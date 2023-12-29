/*
 *   OnTime - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.ontime.core.task

import kotlinx.coroutines.future.await
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import net.luckperms.api.node.NodeType
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.user.Player
import org.anvilpowered.anvil.core.user.PlayerService
import org.anvilpowered.ontime.api.config.OnTimeKeys
import org.anvilpowered.ontime.api.user.OnTimeUserRepository
import org.apache.logging.log4j.Logger

class LuckPermsSyncTaskService(
    registry: Registry,
    onTimeKeys: OnTimeKeys,
    private val playerService: PlayerService,
    private val logger: Logger,
    private val onTimeUserRepository: OnTimeUserRepository,
) {

    private val configRanks = registry[onTimeKeys.RANKS]

    private fun User.clearOnTimeGroups() {
        configRanks.keys.forEach {
            data().remove(Node.builder("group.$it").build())
        }
    }

    private suspend fun syncPlayer(player: Player) {
        val luckperms = LuckPermsProvider.get()
        val user = luckperms.userManager.getUser(player.id)
        if (user == null) {
            logger.warn("User ${player.username} not found in LuckPerms, skipping...")
            return
        }
        val multiplier = user.nodes
            .filter(NodeType.META::matches)
            .map(NodeType.META::cast)
            .firstOrNull { it.metaKey == MULTIPLIER_META_KEY }
            ?.metaValue

        val time = if (multiplier == null) {
            60
        } else {
            try {
                multiplier.toDouble() * 60
            } catch (e: NumberFormatException) {
                logger.warn("An error occurred parsing the time multiplier '$multiplier' for ${user.username}", e)
                return
            }
        }

        val totalTime = onTimeUserRepository.addPlayTime(player.id, time.toLong())
        if (totalTime == null) {
            logger.warn("Could not find OnTime user for ${user.username} in sync task, skipping...")
            return
        }

        val targetRank = configRanks.asSequence()
            .sortedBy { (_, time) -> time }
            .firstOrNull { (_, time) -> totalTime >= time }
            ?.key
        if (targetRank == null) {
            logger.warn("Could not determine target rank for ${user.username} in sync task, skipping...")
            return
        }

        user.clearOnTimeGroups()
        val addedResult = user.data().add(Node.builder("group.$targetRank").build())
        if (addedResult.wasSuccessful()) {
            logger.info("Moved ${user.username} to group $targetRank")
        }
        luckperms.userManager.saveUser(user).await()
        logger.debug("Saved {} to LuckPerms.", user.username)
    }

    suspend fun sync() {
        logger.info("Syncing all players...")
        val players = playerService.getAll().toList()
        players.forEach { syncPlayer(it) }
        logger.info("Finished syncing all (${players.size}) players.")
    }

    companion object {
        private const val MULTIPLIER_META_KEY: String = "ontime-multiplier"
    }
}
