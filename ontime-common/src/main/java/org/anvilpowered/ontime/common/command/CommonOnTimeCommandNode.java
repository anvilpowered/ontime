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

package org.anvilpowered.ontime.common.command;

import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.anvil.api.command.CommandNode;
import org.anvilpowered.anvil.api.command.CommandService;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class CommonOnTimeCommandNode<TCommandExecutor, TCommandSource>
    implements CommandNode<TCommandSource> {

    protected static final List<String> ADD_ALIAS = Arrays.asList("add", "a");
    protected static final List<String> CHECK_ALIAS = Arrays.asList("check", "c", "info", "i");
    protected static final List<String> IMPORT_ALIAS = Collections.singletonList("import");
    protected static final List<String> SET_BONUS_ALIAS = Arrays.asList("setbonus", "sb");
    protected static final List<String> SET_TOTAL_ALIAS = Arrays.asList("set", "s", "settotal", "st");
    protected static final List<String> HELP_ALIAS = Arrays.asList("help", "h");
    protected static final List<String> VERSION_ALIAS = Arrays.asList("version", "v");

    protected static final String ADD_DESCRIPTION = "Add bonus time to a player";
    protected static final String CHECK_DESCRIPTION = "Check playtime";
    protected static final String IMPORT_DESCRIPTION = "Import data from rankupper";
    protected static final String SET_BONUS_DESCRIPTION = "Set bonus playtime";
    protected static final String SET_TOTAL_DESCRIPTION = "Set total playtime";
    protected static final String HELP_DESCRIPTION = "Shows this help page.";
    protected static final String VERSION_DESCRIPTION = "Shows the plugin version";
    protected static final String ROOT_DESCRIPTION = String.format("%s root command", OnTimePluginInfo.name);

    protected static final String ADD_USAGE = "<user> <time>";
    protected static final String CHECK_USAGE = "[<user>]";
    protected static final String IMPORT_USAGE = "<path>";
    protected static final String SET_BONUS_USAGE = "<user> <time>";
    protected static final String SET_TOTAL_USAGE = "<user> <time>";

    protected static final String HELP_COMMAND = "/ontime help";

    private boolean alreadyLoaded;
    protected Map<List<String>, Function<TCommandSource, String>> descriptions;
    protected Map<List<String>, Predicate<TCommandSource>> permissions;
    protected Map<List<String>, Function<TCommandSource, String>> usages;

    @Inject
    protected CommandService<TCommandExecutor, TCommandSource> commandService;

    @Inject
    protected Environment environment;

    protected Registry registry;

    protected CommonOnTimeCommandNode(Registry registry) {
        this.registry = registry;
        registry.whenLoaded(() -> {
            if (alreadyLoaded) return;
            loadCommands();
            alreadyLoaded = true;
        }).register();
        alreadyLoaded = false;
        descriptions = new HashMap<>();
        permissions = new HashMap<>();
        usages = new HashMap<>();
        descriptions.put(ADD_ALIAS, c -> ADD_DESCRIPTION);
        descriptions.put(CHECK_ALIAS, c -> CHECK_DESCRIPTION);
        descriptions.put(IMPORT_ALIAS, c -> IMPORT_DESCRIPTION);
        descriptions.put(SET_BONUS_ALIAS, c -> SET_BONUS_DESCRIPTION);
        descriptions.put(SET_TOTAL_ALIAS, c -> SET_TOTAL_DESCRIPTION);
        descriptions.put(HELP_ALIAS, c -> HELP_DESCRIPTION);
        descriptions.put(VERSION_ALIAS, c -> VERSION_DESCRIPTION);
        usages.put(ADD_ALIAS, c -> ADD_USAGE);
        usages.put(CHECK_ALIAS, c -> CHECK_USAGE);
        usages.put(IMPORT_ALIAS, c -> IMPORT_USAGE);
        usages.put(SET_BONUS_ALIAS, c -> SET_BONUS_USAGE);
        usages.put(SET_TOTAL_ALIAS, c -> SET_TOTAL_USAGE);
    }

    protected abstract void loadCommands();

    private static final String ERROR_MESSAGE = "OnTime command has not been loaded yet";

    @Override
    public Map<List<String>, Function<TCommandSource, String>> getDescriptions() {
        return Objects.requireNonNull(descriptions, ERROR_MESSAGE);
    }

    @Override
    public Map<List<String>, Predicate<TCommandSource>> getPermissions() {
        return Objects.requireNonNull(permissions, ERROR_MESSAGE);
    }

    @Override
    public Map<List<String>, Function<TCommandSource, String>> getUsages() {
        return Objects.requireNonNull(usages, ERROR_MESSAGE);
    }

    @Override
    public String getName() {
        return OnTimePluginInfo.id;
    }
}
