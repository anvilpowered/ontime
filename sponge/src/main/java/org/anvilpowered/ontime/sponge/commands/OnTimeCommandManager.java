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
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.ontime.api.data.key.MSOnTimeKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnTimeCommandManager {

    public static Map<List<String>, CommandSpec> subCommands = new HashMap<>();

    @Inject
    OnTimeAddCommand onTimeAddCommand;

    @Inject
    OnTimeBaseCommand onTimeBaseCommand;

    @Inject
    OnTimeCheckCommand onTimeCheckCommand;

    @Inject
    OnTimeImportCommand onTimeImportCommand;

    @Inject
    OnTimeSetBonusCommand onTimeSetBonusCommand;

    @Inject
    OnTimeSetCommand onTimeSetCommand;

    @Inject
    Environment environment;

    @Inject
    public OnTimeCommandManager(Registry registry) {
        registry.addRegistryLoadedListener(this::register);
    }

    public void register() {
        Map<List<String>, CommandSpec> subCommands = new HashMap<>();

        subCommands.put(Arrays.asList("add", "a"), CommandSpec.builder()
            .description(Text.of("Add bonus time to a player"))
            .permission(MSOnTimeKeys.EDIT_PERMISSION.getFallbackValue())
            .arguments(
                GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                GenericArguments.integer(Text.of("time"))
            )
            .executor(onTimeAddCommand)
            .build()
        );

        subCommands.put(Arrays.asList("check", "c", "info", "i"), CommandSpec.builder()
            .description(Text.of("Check play time"))
            .permission(MSOnTimeKeys.CHECK_PERMISSION.getFallbackValue())
            .arguments(
                GenericArguments.optional(
                    GenericArguments.requiringPermission(
                        GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                        MSOnTimeKeys.CHECK_EXTENDED_PERMISSION.getFallbackValue()
                    )
                )
            )
            .executor(onTimeCheckCommand)
            .build()
        );

        subCommands.put(Collections.singletonList("import"), CommandSpec.builder()
            .description(Text.of("Import data from rankupper"))
            .permission(MSOnTimeKeys.IMPORT_PERMISSION.getFallbackValue())
            .arguments(GenericArguments.string(Text.of("path")))
            .executor(onTimeImportCommand)
            .build());

        subCommands.put(Arrays.asList("setbonus", "sb"), CommandSpec.builder()
            .description(Text.of("Set bonus playtime"))
            .permission(MSOnTimeKeys.EDIT_PERMISSION.getFallbackValue())
            .arguments(
                GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                GenericArguments.integer(Text.of("time"))
            )
            .executor(onTimeSetBonusCommand)
            .build()
        );

        subCommands.put(Arrays.asList("set", "s", "settotal", "st"), CommandSpec.builder()
            .description(Text.of("Set total playtime"))
            .permission(MSOnTimeKeys.EDIT_PERMISSION.getFallbackValue())
            .arguments(
                GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                GenericArguments.integer(Text.of("time"))
            )
            .executor(onTimeSetCommand)
            .build()
        );

        CommandSpec mainCommand = CommandSpec.builder()
            .description(Text.of("Base command"))
            .executor(onTimeBaseCommand)
            .children(subCommands)
            .build();

        Sponge.getCommandManager().register(environment.getPlugin(), mainCommand, "msontime", "ontime", "ot", "playtime");
        OnTimeCommandManager.subCommands = subCommands;
    }
}
