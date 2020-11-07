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
package org.anvilpowered.ontime.velocity.command

import com.google.inject.Inject
import com.google.inject.Singleton
import com.velocitypowered.api.command.Command
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.ontime.common.command.CommonOnTimeCommandNode
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo

@Singleton
class VelocityOnTimeCommandNode @Inject constructor(
    registry: Registry
) : CommonOnTimeCommandNode<Command, CommandSource>(registry) {
    @Inject
    private lateinit var proxyServer: ProxyServer

    @Inject
    private lateinit var onTimeAddCommand: VelocityAddCommand

    @Inject
    private lateinit var onTimeCheckCommand: VelocityCheckCommand

    @Inject
    private lateinit var onTimeSetBonusCommand: VelocitySetBonusCommand

    @Inject
    private lateinit var onTimeSetTotalCommand: VelocitySetTotalCommand

    override fun loadCommands() {
        val subCommands: MutableMap<List<String>, Command> = mutableMapOf()
        subCommands[ADD_ALIAS] = onTimeAddCommand
        subCommands[CHECK_ALIAS] = onTimeCheckCommand
        subCommands[SET_BONUS_ALIAS] = onTimeSetBonusCommand
        subCommands[SET_TOTAL_ALIAS] = onTimeSetTotalCommand
        subCommands[HELP_ALIAS] = commandService.generateHelpCommand(this)
        subCommands[VERSION_ALIAS] = commandService.generateVersionCommand(HELP_COMMAND)
        proxyServer.commandManager.register(
            proxyServer.commandManager.metaBuilder(OnTimePluginInfo.id).aliases("ot").build(),
            commandService.generateRoutingCommand(
                commandService.generateRootCommand(HELP_COMMAND), subCommands, false
            )
        )
    }
}
