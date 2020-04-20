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

package org.anvilpowered.ontime.velocity.module;

import com.google.inject.TypeLiteral;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.anvilpowered.anvil.api.command.CommandNode;
import org.anvilpowered.ontime.api.tasks.SyncTaskService;
import org.anvilpowered.ontime.common.module.CommonModule;
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo;
import org.anvilpowered.ontime.velocity.command.VelocityOnTimeCommandNode;
import org.anvilpowered.ontime.velocity.task.VelocitySyncTaskService;

import java.io.File;
import java.nio.file.Paths;

public class VelocityModule extends CommonModule<Player, Player, TextComponent, CommandSource> {

    @Override
    protected void configure() {
        super.configure();
        bind(new TypeLiteral<CommandNode<CommandSource>>() {
        }).to(VelocityOnTimeCommandNode.class);
        File configFilesLocation = Paths.get("plugins/" + OnTimePluginInfo.id).toFile();
        if (!configFilesLocation.exists()) {
            if (!configFilesLocation.mkdirs()) {
                throw new IllegalStateException("Unable to create config directory");
            }
        }
        bind(new TypeLiteral<ConfigurationLoader<CommentedConfigurationNode>>() {
        }).toInstance(HoconConfigurationLoader.builder().setPath(Paths.get(configFilesLocation + "/ontime.conf")).build());
        bind(SyncTaskService.class).to(VelocitySyncTaskService.class);
    }
}
