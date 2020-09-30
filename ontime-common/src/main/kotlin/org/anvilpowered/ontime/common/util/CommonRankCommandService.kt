/*
 *   OnTime - AnvilPowered
 *   Copyright (C) 2020
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.ontime.common.util

import com.google.inject.Inject
import org.anvilpowered.anvil.api.command.CommandExecuteService
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.TimeFormatService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.ontime.api.registry.OnTimeKeys
import org.anvilpowered.ontime.api.util.RankCommandService
import java.time.Duration
import java.util.UUID

class CommonRankCommandService<TUser, TPlayer> : RankCommandService {

    @Inject
    private lateinit var commandExecuteService: CommandExecuteService

    @Inject
    private lateinit var registry: Registry

    @Inject
    private lateinit var timeFormatService: TimeFormatService

    @Inject
    private lateinit var userService: UserService<TUser, TPlayer>

    override fun runCommands(userUUID: UUID, rank: String, time: Long) {
        val commandMap = registry.get(OnTimeKeys.COMMANDS).orElse(null) ?: return
        for ((regex, commands) in commandMap) {
            if (regex.toRegex().matches(rank)) {
                for (command in commands) {
                    val toRun = command
                        .replace("%player%", userService.getUserName(userUUID).join().orElse("null"))
                        .replace("%rank%", rank)
                        .replace("%time%", timeFormatService.format(Duration.ofSeconds(time)).toString())
                    commandExecuteService.execute(toRun)
                }
            }
        }
    }
}
