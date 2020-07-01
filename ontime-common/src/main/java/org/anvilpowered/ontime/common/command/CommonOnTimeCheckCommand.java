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

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public class CommonOnTimeCheckCommand<TUser, TPlayer, TString, TCommandSource> {

    @Inject
    protected MemberManager<TString> memberManager;

    @Inject
    private PermissionService permissionService;

    @Inject
    private PluginInfo<TString> pluginInfo;

    @Inject
    protected Registry registry;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private UserService<TUser, TPlayer> userService;

    public void sendError(TCommandSource source) {
        textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("Specify user or run as player!")
            .sendTo(source);
    }

    public void execute(TCommandSource source, String[] context,
                        Class<TPlayer> playerClass) {
        final boolean isPlayer = playerClass.isAssignableFrom(source.getClass());
        if (!permissionService.hasPermission(source,
            registry.getOrDefault(OnTimeKeys.CHECK_PERMISSION))) {
            textService.builder()
                .append(pluginInfo.getPrefix())
                .red().append("You do not have permission for this command!")
                .sendTo(source);
            return;
        }
        final boolean hasExtended = permissionService.hasPermission(source,
            registry.getOrDefault(OnTimeKeys.CHECK_EXTENDED_PERMISSION));

        CompletableFuture<Optional<UUID>> futureUUID;
        if (context.length == 1 && hasExtended) {
            futureUUID = userService.getUUID(context[0]);
        } else {
            futureUUID = CompletableFuture.completedFuture(Optional.empty());
        }
        futureUUID.thenAcceptAsync(optionalUserUUID -> {
            if (optionalUserUUID.isPresent()) {
                memberManager.infoExtended(optionalUserUUID.get())
                    .thenAcceptAsync(result -> textService.send(result, source));
            } else if (isPlayer) {
                UUID userUUID = userService.getUUID((TUser) source);
                if (hasExtended) {
                    memberManager.infoExtended(userUUID)
                        .thenAcceptAsync(result -> textService.send(result, source));
                } else {
                    memberManager.info(userUUID)
                        .thenAcceptAsync(result -> textService.send(result, source));
                }
            } else {
                sendError(source);
            }
        });
    }
}
