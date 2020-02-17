/*
 *   OnTime - AnvilPowered
 *   Copyright (C) 2020 Cableguy20
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.ontime.sponge.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class OnTimeBaseCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        List<Text> helpList = new ArrayList<>();
        OnTimeCommandManager.subCommands.forEach((aliases, commandSpec) -> {
            if (!commandSpec.getShortDescription(source).isPresent()) return;
            String subCommand = aliases.toString().replace("[", "").replace("]", "");
            Text commandHelp = Text.builder()
                .append(Text.builder()
                    .append(Text.of(TextColors.GREEN, "/ontime ", subCommand))
                    .build())
                .append(Text.builder()
                    .append(Text.of(TextColors.GOLD, " - " + commandSpec.getShortDescription(source).get().toPlain() + "\n"))
                    .build())
                .append(Text.builder()
                    .append(Text.of(TextColors.GRAY, "Usage: /ontime ", subCommand, " ", commandSpec.getUsage(source).toPlain()))
                    .build())
                .build();
            helpList.add(commandHelp);
        });
        helpList.sort(Text::compareTo);
        Sponge.getServiceManager().provide(PaginationService.class)
            .orElseThrow(() -> new CommandException(Text.of("Missing pagination service")))
            .builder()
            .title(Text.of(TextColors.GOLD, "OnTime - MilspecSG"))
            .padding(Text.of(TextColors.DARK_GREEN, "-"))
            .contents(helpList)
            .linesPerPage(20)
            .build()
            .sendTo(source);
        return CommandResult.success();
    }
}
