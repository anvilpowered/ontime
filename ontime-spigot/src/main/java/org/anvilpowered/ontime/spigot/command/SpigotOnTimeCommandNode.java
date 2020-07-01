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

package org.anvilpowered.ontime.spigot.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.ontime.common.command.CommonOnTimeCommandNode;
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo;
import org.anvilpowered.ontime.spigot.OnTimeSpigot;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Singleton
public class SpigotOnTimeCommandNode
    extends CommonOnTimeCommandNode<CommandExecutor, CommandSender> {

    @Inject
    private SpigotOnTimeAddCommand onTimeAddCommand;

    @Inject
    private SpigotOnTimeCheckCommand onTimeCheckCommand;

    @Inject
    private SpigotOnTimeSetBonusCommand onTimeSetBonusCommand;

    @Inject
    private SpigotOnTimeSetTotalCommand onTimeSetTotalCommand;

    @Inject
    private OnTimeSpigot plugin;

    @Inject
    public SpigotOnTimeCommandNode(Registry registry) {
        super(registry);
    }

    @Override
    protected void loadCommands() {
        Map<List<String>, CommandExecutor> subCommands = new HashMap<>();

        subCommands.put(ADD_ALIAS, onTimeAddCommand);
        subCommands.put(CHECK_ALIAS, onTimeCheckCommand);
        subCommands.put(SET_BONUS_ALIAS, onTimeSetBonusCommand);
        subCommands.put(SET_TOTAL_ALIAS, onTimeSetTotalCommand);
        subCommands.put(HELP_ALIAS, commandService.generateHelpCommand(this));
        subCommands.put(VERSION_ALIAS, commandService.generateVersionCommand(HELP_COMMAND));

        PluginCommand root = plugin.getCommand(OnTimePluginInfo.id);
        Objects.requireNonNull(root, "OnTime command not registered");
        root.setExecutor(commandService.generateRoutingCommand(
            commandService.generateRootCommand(HELP_COMMAND), subCommands, false));
    }
}
