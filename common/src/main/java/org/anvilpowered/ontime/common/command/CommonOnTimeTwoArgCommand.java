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

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.ontime.api.data.key.OnTimeKeys;
import org.anvilpowered.ontime.api.member.MemberManager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class CommonOnTimeTwoArgCommand<TUser, TPlayer, TString, TCommandSource> {

    @Inject
    protected MemberManager<TString> memberManager;

    @Inject
    protected PermissionService permissionService;

    @Inject
    private PluginInfo<TString> pluginInfo;

    @Inject
    protected Registry registry;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private UserService<TUser, TPlayer> userService;

    public void send(TCommandSource source, String[] context, String command,
                     BiFunction<UUID, String, CompletableFuture<TString>> function) {
        if (!permissionService.hasPermission(source,
            registry.getOrDefault(OnTimeKeys.EDIT_PERMISSION))) {
            textService.builder()
                .append(pluginInfo.getPrefix())
                .red().append("You do not have permission for this command!")
                .sendTo(source);
            return;
        }
        if (context.length != 2) {
            textService.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Not enough arguments!")
                .append("\n", command, " ").appendJoining(" ", context)
                .append("\n^\nUsage: /ontime ", command, " <user> <time...>")
                .sendTo(source);
            return;
        }
        userService.getUUID(context[0]).thenAcceptAsync(userUUID -> {
            if (!userUUID.isPresent()) {
                textService.builder()
                    .append(pluginInfo.getPrefix())
                    .red().append("No values matching pattern '", context[0], "' present for user!")
                    .append("\n", command, " ").appendJoining(" ", context)
                    .append("\n").appendCount(command.length() + 1, " ")
                    .append("^\nUsage: /ontime ", command, " <user> <time...>")
                    .sendTo(source);
                return;
            }
            function.apply(userUUID.get(), context[1])
                .thenAcceptAsync(result -> textService.send(result, source));
        });
    }
}
