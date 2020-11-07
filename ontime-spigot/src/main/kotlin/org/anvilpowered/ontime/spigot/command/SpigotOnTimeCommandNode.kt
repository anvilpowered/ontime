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
package org.anvilpowered.ontime.spigot.command

import com.google.inject.Inject
import com.google.inject.Singleton
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.ontime.common.command.CommonOnTimeCommandNode
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo
import org.anvilpowered.ontime.spigot.OnTimeSpigot
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.HashMap

@Singleton
class SpigotOnTimeCommandNode @Inject constructor(
    registry: Registry
) : CommonOnTimeCommandNode<CommandExecutor, CommandSender>(registry) {
    @Inject
    private lateinit var onTimeAddCommand: SpigotAddCommand

    @Inject
    private lateinit var onTimeCheckCommand: SpigotCheckCommand

    @Inject
    private lateinit var onTimeSetBonusCommand: SpigotSetBonusCommand

    @Inject
    private lateinit var onTimeSetTotalCommand: SpigotSetTotalCommand

    @Inject
    private lateinit var plugin: OnTimeSpigot

    override fun loadCommands() {
        val subCommands: MutableMap<List<String>, CommandExecutor> = HashMap()
        subCommands[ADD_ALIAS] = onTimeAddCommand
        subCommands[CHECK_ALIAS] = onTimeCheckCommand
        subCommands[SET_BONUS_ALIAS] = onTimeSetBonusCommand
        subCommands[SET_TOTAL_ALIAS] = onTimeSetTotalCommand
        subCommands[HELP_ALIAS] = commandService.generateHelpCommand(this)
        subCommands[VERSION_ALIAS] = commandService.generateVersionCommand(HELP_COMMAND)
        plugin.getCommand(OnTimePluginInfo.id)!!.setExecutor(
            commandService.generateRoutingCommand(
                commandService.generateRootCommand(HELP_COMMAND), subCommands, false
            )
        )
    }
}
