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
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.anvil.api.command.CommandNode;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.CommandService;
import org.anvilpowered.ontime.api.data.key.OnTimeKeys;
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OnTimeSpongeCommandNode implements CommandNode<CommandSpec> {

    private boolean alreadyLoaded;
    private CommandSpec command;
    private Map<List<String>, CommandSpec> subCommands;

    @Inject
    OnTimeAddCommand onTimeAddCommand;

    @Inject
    OnTimeCheckCommand onTimeCheckCommand;

    @Inject
    OnTimeImportCommand onTimeImportCommand;

    @Inject
    OnTimeSetBonusCommand onTimeSetBonusCommand;

    @Inject
    OnTimeSetCommand onTimeSetCommand;

    @Inject
    private CommandService<CommandSpec, CommandExecutor, CommandSource> commandService;

    @Inject
    private Environment environment;

    @Inject
    public OnTimeSpongeCommandNode(Registry registry) {
        registry.addRegistryLoadedListener(this::register);
        this.alreadyLoaded = false;
    }

    public void register() {
        if (alreadyLoaded) return;
        alreadyLoaded = true;

        subCommands = new HashMap<>();

        subCommands.put(Arrays.asList("add", "a"), CommandSpec.builder()
            .description(Text.of("Add bonus time to a player"))
            .permission(OnTimeKeys.EDIT_PERMISSION.getFallbackValue())
            .arguments(
                GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                GenericArguments.remainingJoinedStrings(Text.of("time"))
            )
            .executor(onTimeAddCommand)
            .build()
        );

        subCommands.put(Arrays.asList("check", "c", "info", "i"), CommandSpec.builder()
            .description(Text.of("Check play time"))
            .permission(OnTimeKeys.CHECK_PERMISSION.getFallbackValue())
            .arguments(
                GenericArguments.optional(
                    GenericArguments.requiringPermission(
                        GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                        OnTimeKeys.CHECK_EXTENDED_PERMISSION.getFallbackValue()
                    )
                )
            )
            .executor(onTimeCheckCommand)
            .build()
        );

        subCommands.put(Collections.singletonList("import"), CommandSpec.builder()
            .description(Text.of("Import data from rankupper"))
            .permission(OnTimeKeys.IMPORT_PERMISSION.getFallbackValue())
            .arguments(GenericArguments.remainingJoinedStrings(Text.of("path")))
            .executor(onTimeImportCommand)
            .build());

        subCommands.put(Arrays.asList("setbonus", "sb"), CommandSpec.builder()
            .description(Text.of("Set bonus playtime"))
            .permission(OnTimeKeys.EDIT_PERMISSION.getFallbackValue())
            .arguments(
                GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                GenericArguments.remainingJoinedStrings(Text.of("time"))
            )
            .executor(onTimeSetBonusCommand)
            .build()
        );

        subCommands.put(Arrays.asList("set", "s", "settotal", "st"), CommandSpec.builder()
            .description(Text.of("Set total playtime"))
            .permission(OnTimeKeys.EDIT_PERMISSION.getFallbackValue())
            .arguments(
                GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                GenericArguments.remainingJoinedStrings(Text.of("time"))
            )
            .executor(onTimeSetCommand)
            .build()
        );

        subCommands.put(Collections.singletonList("help"), CommandSpec.builder()
            .description(Text.of("Help command"))
            .executor(commandService.generateHelpCommand(this))
            .build()
        );

        subCommands.put(Collections.singletonList("version"), CommandSpec.builder()
            .description(Text.of("Version command"))
            .executor(commandService.generateVersionCommand("/ontime help"))
            .build()
        );

        command = CommandSpec.builder()
            .description(Text.of("Root command"))
            .executor(commandService.generateRootCommand("/ontime help"))
            .children(subCommands)
            .build();

        Sponge.getCommandManager()
            .register(environment.getPlugin(), command, OnTimePluginInfo.id, "ot");
    }

    private static final String ERROR_MESSAGE = "OnTime command has not been loaded yet";

    @Override
    public Map<List<String>, CommandSpec> getCommands() {
        return Objects.requireNonNull(subCommands, ERROR_MESSAGE);
    }

    @Override
    public CommandSpec getCommand() {
        return Objects.requireNonNull(command, ERROR_MESSAGE);
    }

    @Override
    public String getName() {
        return OnTimePluginInfo.id;
    }
}
