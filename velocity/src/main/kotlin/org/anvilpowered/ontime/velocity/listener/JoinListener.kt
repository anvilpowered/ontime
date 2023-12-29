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

package org.anvilpowered.ontime.velocity.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.PostLoginEvent
import kotlinx.coroutines.runBlocking
import org.anvilpowered.ontime.api.user.OnTimeUser
import org.anvilpowered.ontime.api.user.OnTimeUserRepository
import org.anvilpowered.ontime.api.user.bonusTimeFormatted
import org.anvilpowered.ontime.api.user.playTimeFormatted
import org.anvilpowered.ontime.api.user.totalTimeFormatted
import org.apache.logging.log4j.Logger

class JoinListener(private val logger: Logger, private val onTimeUserRepository: OnTimeUserRepository) {

    @Subscribe
    fun onPlayerJoin(event: PostLoginEvent) = runBlocking {
        val player = event.player
        val result = onTimeUserRepository.put(
            OnTimeUser.CreateDto(
                id = player.uniqueId,
                username = player.username,
            ),
        )

        if (result.created) {
            logger.info("Created new OnTime user ${player.username} (${player.uniqueId})")
        } else {

        }
    }
}
