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
package org.anvilpowered.ontime.common.command

import com.google.inject.Inject
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.ontime.api.member.MemberManager
import org.anvilpowered.ontime.api.plugin.PluginMessages
import org.anvilpowered.ontime.api.registry.OnTimeKeys
import java.util.Optional
import java.util.UUID
import java.util.concurrent.CompletableFuture

open class CommonCheckCommand<
    TUser : Any,
    TPlayer : Any,
    TString : Any,
    TCommandSource : Any
    >(
    private val playerClass: Class<TPlayer>
) {

    @Inject
    protected lateinit var memberManager: MemberManager<TString>

    @Inject
    private lateinit var permissionService: PermissionService

    @Inject
    private lateinit var pluginInfo: PluginInfo<TString>

    @Inject
    private lateinit var pluginMessages: PluginMessages<TString>

    @Inject
    protected lateinit var registry: Registry

    @Inject
    protected lateinit var textService: TextService<TString, TCommandSource>

    @Inject
    private lateinit var userService: UserService<TUser, TPlayer>

    private val error: TString by lazy {
        textService.builder()
            .append(pluginInfo.prefix)
            .red().append("Specify user or run as player!")
            .build()
    }

    private fun testPermission(source: Any?): Boolean {
        return permissionService.hasPermission(source ?: return false, registry.getOrDefault(OnTimeKeys.CHECK_PERMISSION))
    }

    private fun hasNoPerms(source: TCommandSource): Boolean {
        if (!testPermission(source)) {
            textService.send(pluginMessages.noPermission, source)
            return true
        }
        return false
    }

    open fun execute(
        source: TCommandSource,
        context: Array<String>
    ) {
        if (hasNoPerms(source)) return
        val isPlayer = playerClass.isAssignableFrom(source.javaClass)
        val hasExtended = permissionService.hasPermission(source, registry.getOrDefault(OnTimeKeys.CHECK_EXTENDED_PERMISSION))
        val futureUUID = if (context.size == 1 && hasExtended) {
            userService.getUUID(context[0])
        } else {
            CompletableFuture.completedFuture(Optional.empty())
        }
        futureUUID.thenAcceptAsync { optionalUserUUID: Optional<UUID> ->
            if (optionalUserUUID.isPresent) {
                memberManager.infoExtended(optionalUserUUID.get())
                    .thenAcceptAsync { textService.send(it, source) }
            } else if (isPlayer) {
                val userUUID = userService.getUUID(source as TUser)
                if (hasExtended) {
                    memberManager.infoExtended(userUUID)
                } else {
                    memberManager.info(userUUID)
                }.thenAcceptAsync { textService.send(it, source) }
            } else {
                textService.send(error, source)
            }
        }
    }

    open fun suggest(source: TCommandSource, context: Array<String>): List<String> {
        if (!testPermission(source)) return listOf()
        return userService.matchPlayerNames(context, 0, 1)
    }
}
