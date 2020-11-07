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
import org.anvilpowered.anvil.api.asNullable
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.ontime.api.member.MemberManager
import org.anvilpowered.ontime.api.plugin.PluginMessages
import org.anvilpowered.ontime.api.registry.OnTimeKeys
import java.util.UUID
import java.util.concurrent.CompletableFuture

open class CommonTwoArgCommand<TUser, TPlayer, TString, TCommandSource> {

    @Inject
    protected lateinit var memberManager: MemberManager<TString>

    @Inject
    protected lateinit var permissionService: PermissionService

    @Inject
    private lateinit var pluginInfo: PluginInfo<TString>

    @Inject
    private lateinit var pluginMessages: PluginMessages<TString>

    @Inject
    protected lateinit var registry: Registry

    @Inject
    private lateinit var textService: TextService<TString, TCommandSource>

    @Inject
    private lateinit var userService: UserService<TUser, TPlayer>

    private fun testPermission(source: Any?): Boolean {
        return permissionService.hasPermission(source ?: return false, registry.getOrDefault(OnTimeKeys.EDIT_PERMISSION))
    }

    private fun hasNoPerms(source: TCommandSource): Boolean {
        if (!testPermission(source)) {
            textService.send(pluginMessages.noPermission, source)
            return true
        }
        return false
    }

    fun execute(
        source: TCommandSource,
        context: Array<String>,
        command: String,
        function: (UUID, String) -> CompletableFuture<TString>
    ) {
        if (hasNoPerms(source)) return
        if (context.size < 2) {
            textService.builder()
                .append(pluginInfo.prefix)
                .red().append("Not enough arguments!")
                .append("\n", command, " ").appendJoining(" ", *context)
                .append("\n^\nUsage: /ontime ", command, " <user> <time...>")
                .sendTo(source)
            return
        }

        userService.getUUID(context[0]).asNullable().thenAcceptAsync { userUUID: UUID? ->
            if (userUUID != null) {
                val sb = StringBuilder()
                sb.append(context[1])
                for (i in 2 until context.size) {
                    sb.append(" ").append(context[i])
                }
                function(userUUID, sb.toString()).thenAcceptAsync { textService.send(it, source) }
                return@thenAcceptAsync
            }
            textService.builder()
                .append(pluginInfo.prefix)
                .red().append("No values matching pattern '", context[0], "' present for user!")
                .append("\n", command, " ").appendJoining(" ", *context)
                .append("\n").appendCount(command.length + 1, " ")
                .append("^\nUsage: /ontime ", command, " <user> <time...>")
                .sendTo(source)
            return@thenAcceptAsync
        }
    }

    open fun suggest(source: TCommandSource, context: Array<String>): List<String> {
        if (!testPermission(source)) return listOf()
        return userService.matchPlayerNames(context, 0, 1)
    }
}
