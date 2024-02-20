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

import org.anvilpowered.anvil.core.command.CommandExecutor
import org.anvilpowered.anvil.core.command.format
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.user.Player
import org.anvilpowered.ontime.api.config.OnTimeKeys
import org.apache.logging.log4j.Logger
import java.time.Duration

class RankCommandService(
    private val registry: Registry,
    private val onTimeKeys: OnTimeKeys,
    private val commandExecutor: CommandExecutor,
    private val logger: Logger,
) {

    suspend fun runCommands(player: Player, rank: String, time: Long) {
        logger.debug("Running commands for player {} with rank {} and time {}", player.username, rank, time)
        for ((regex, commands) in registry[onTimeKeys.RANK_COMMANDS]) {
            if (regex.toRegex().matches(rank)) {
                for (command in commands) {
                    val toRun = command
                        .replace("%id%", player.id.toString())
                        .replace("%username%", player.username)
                        .replace("%rank%", rank)
                        .replace("%time%", Duration.ofSeconds(time).format())
                    commandExecutor.executeAsConsole(toRun)
                }
            }
        }
    }
}
