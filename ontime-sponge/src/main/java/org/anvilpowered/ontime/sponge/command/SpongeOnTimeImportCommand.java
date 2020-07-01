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

package org.anvilpowered.ontime.sponge.command;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.ontime.api.util.DataImportService;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class SpongeOnTimeImportCommand implements CommandExecutor {

    @Inject
    private DataImportService importService;

    @Inject
    private PluginInfo<Text> pluginInfo;

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalPath = context.getOne(Text.of("path"));
        if (optionalPath.isPresent()) {
            String inputPath = optionalPath.get();
            Path path = Paths.get(inputPath);

            if (path.toFile().exists()) {
                source.sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.GREEN, "Starting Rankup import!"));
                importService.importData(path);
            } else {
                source.sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.YELLOW, "Could not find the specified file." +
                    "\nDid you mean:", TextColors.GOLD, "\"config/rankup/playerstats.conf\" ?"));
            }
        } else {
            throw new CommandException(Text.of("Please specify a path!"));
        }
        return CommandResult.success();
    }
}
