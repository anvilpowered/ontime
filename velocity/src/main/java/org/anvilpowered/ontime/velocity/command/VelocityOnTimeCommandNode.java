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

package org.anvilpowered.ontime.velocity.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.ontime.common.command.CommonOnTimeCommandNode;
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class VelocityOnTimeCommandNode
    extends CommonOnTimeCommandNode<Command, CommandSource> {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private VelocityOnTimeAddCommand onTimeAddCommand;

    @Inject
    private VelocityOnTimeCheckCommand onTimeCheckCommand;

    @Inject
    private VelocityOnTimeSetBonusCommand onTimeSetBonusCommand;

    @Inject
    private VelocityOnTimeSetTotalCommand onTimeSetTotalCommand;

    @Inject
    public VelocityOnTimeCommandNode(Registry registry) {
        super(registry);
    }

    @Override
    protected void loadCommands() {
        Map<List<String>, Command> subCommands = new HashMap<>();

        subCommands.put(ADD_ALIAS, onTimeAddCommand);
        subCommands.put(CHECK_ALIAS, onTimeCheckCommand);
        subCommands.put(SET_BONUS_ALIAS, onTimeSetBonusCommand);
        subCommands.put(SET_TOTAL_ALIAS, onTimeSetTotalCommand);
        subCommands.put(HELP_ALIAS, commandService.generateHelpCommand(this));
        subCommands.put(VERSION_ALIAS, commandService.generateVersionCommand(HELP_COMMAND));

        proxyServer.getCommandManager().register(OnTimePluginInfo.id,
            commandService.generateRoutingCommand(
                commandService.generateRootCommand(HELP_COMMAND), subCommands, false),
            "ot");
    }
}
