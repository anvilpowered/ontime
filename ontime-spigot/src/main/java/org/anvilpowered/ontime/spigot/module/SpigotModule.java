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

package org.anvilpowered.ontime.spigot.module;

import com.google.inject.TypeLiteral;
import net.md_5.bungee.api.chat.TextComponent;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.anvilpowered.anvil.api.command.CommandNode;
import org.anvilpowered.ontime.api.task.SyncTaskService;
import org.anvilpowered.ontime.common.module.CommonModule;
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo;
import org.anvilpowered.ontime.spigot.command.SpigotOnTimeCommandNode;
import org.anvilpowered.ontime.spigot.task.SpigotSyncTaskService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Paths;

public class SpigotModule extends CommonModule<Player, Player, TextComponent, CommandSender> {

    @Override
    protected void configure() {
        super.configure();
        bind(new TypeLiteral<CommandNode<CommandSender>>() {
        }).to(SpigotOnTimeCommandNode.class);
        File configFilesLocation = Paths.get("plugins/" + OnTimePluginInfo.id).toFile();
        if (!configFilesLocation.exists()) {
            if (!configFilesLocation.mkdirs()) {
                throw new IllegalStateException("Unable to create config directory");
            }
        }
        bind(new TypeLiteral<ConfigurationLoader<CommentedConfigurationNode>>() {
        }).toInstance(HoconConfigurationLoader.builder().setPath(Paths.get(configFilesLocation + "/ontime.conf")).build());
        bind(SyncTaskService.class).to(SpigotSyncTaskService.class);
    }
}
