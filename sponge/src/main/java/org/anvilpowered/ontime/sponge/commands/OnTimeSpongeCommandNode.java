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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.ontime.api.data.key.OnTimeKeys;
import org.anvilpowered.ontime.common.command.CommonOnTimeCommandNode;
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class OnTimeSpongeCommandNode
    extends CommonOnTimeCommandNode<CommandExecutor, CommandSource> {

    @Inject
    private OnTimeAddCommand onTimeAddCommand;

    @Inject
    private OnTimeCheckCommand onTimeCheckCommand;

    @Inject
    private OnTimeImportCommand onTimeImportCommand;

    @Inject
    private OnTimeSetBonusCommand onTimeSetBonusCommand;

    @Inject
    private OnTimeSetCommand onTimeSetCommand;

    @Inject
    public OnTimeSpongeCommandNode(Registry registry) {
        super(registry);
    }

    @Override
    protected void loadCommands() {
        Map<List<String>, CommandSpec> subCommands = new HashMap<>();

        subCommands.put(ADD_ALIAS, CommandSpec.builder()
            .description(Text.of(ADD_DESCRIPTION))
            .permission(registry.getOrDefault(OnTimeKeys.EDIT_PERMISSION))
            .arguments(
                GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                GenericArguments.remainingJoinedStrings(Text.of("time"))
            )
            .executor(onTimeAddCommand)
            .build()
        );

        subCommands.put(CHECK_ALIAS, CommandSpec.builder()
            .description(Text.of(CHECK_DESCRIPTION))
            .permission(registry.getOrDefault(OnTimeKeys.CHECK_PERMISSION))
            .arguments(
                GenericArguments.optional(
                    GenericArguments.requiringPermission(
                        GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                        registry.getOrDefault(OnTimeKeys.CHECK_EXTENDED_PERMISSION)
                    )
                )
            )
            .executor(onTimeCheckCommand)
            .build()
        );

        subCommands.put(IMPORT_ALIAS, CommandSpec.builder()
            .description(Text.of(IMPORT_DESCRIPTION))
            .permission(registry.getOrDefault(OnTimeKeys.IMPORT_PERMISSION))
            .arguments(GenericArguments.remainingJoinedStrings(Text.of("path")))
            .executor(onTimeImportCommand)
            .build());

        subCommands.put(SET_BONUS_ALIAS, CommandSpec.builder()
            .description(Text.of(SET_BONUS_DESCRIPTION))
            .permission(registry.getOrDefault(OnTimeKeys.EDIT_PERMISSION))
            .arguments(
                GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                GenericArguments.remainingJoinedStrings(Text.of("time"))
            )
            .executor(onTimeSetBonusCommand)
            .build()
        );

        subCommands.put(SET_TOTAL_ALIAS, CommandSpec.builder()
            .description(Text.of(SET_TOTAL_DESCRIPTION))
            .permission(registry.getOrDefault(OnTimeKeys.EDIT_PERMISSION))
            .arguments(
                GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                GenericArguments.remainingJoinedStrings(Text.of("time"))
            )
            .executor(onTimeSetCommand)
            .build()
        );

        subCommands.put(HELP_ALIAS, CommandSpec.builder()
            .description(Text.of(HELP_COMMAND))
            .executor(commandService.generateHelpCommand(this))
            .build()
        );

        subCommands.put(VERSION_ALIAS, CommandSpec.builder()
            .description(Text.of(VERSION_DESCRIPTION))
            .executor(commandService.generateVersionCommand(HELP_COMMAND))
            .build()
        );

        CommandSpec command = CommandSpec.builder()
            .description(Text.of(ROOT_DESCRIPTION))
            .executor(commandService.generateRootCommand(HELP_COMMAND))
            .children(subCommands)
            .build();

        Sponge.getCommandManager()
            .register(environment.getPlugin(), command, OnTimePluginInfo.id, "ot");
    }
}
