/*
 *   OnTime - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.ontime.core.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.core.command.CommandSource
import org.anvilpowered.anvil.core.command.extractPlayerSource
import org.anvilpowered.anvil.core.user.Player
import org.anvilpowered.kbrig.argument.StringArgumentType
import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.builder.RequiredArgumentBuilder
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.context.CommandContextScopeDsl
import org.anvilpowered.kbrig.context.CommandExecutionScope
import org.anvilpowered.kbrig.context.executesScoped
import org.anvilpowered.kbrig.context.get
import org.anvilpowered.kbrig.context.yieldError
import org.anvilpowered.kbrig.suggestion.suggestsScoped
import org.anvilpowered.ontime.api.PluginMessages
import org.anvilpowered.ontime.api.user.OnTimeUser
import org.anvilpowered.ontime.api.user.OnTimeUserRepository

fun ArgumentBuilder.Companion.requireOnTimeUserArgument(
    onTimeUserRepository: OnTimeUserRepository,
    argumentName: String = "user",
    command: suspend (context: CommandContext<CommandSource>, onTimeUser: OnTimeUser) -> Int
): RequiredArgumentBuilder<CommandSource, String> =
    required<CommandSource, String>(argumentName, StringArgumentType.SingleWord)
        .suggestOnTimeUserArgument(onTimeUserRepository)
        .executesScoped { yield(command(context, extractOnTimeUserArgument(onTimeUserRepository, argumentName))) }

fun ArgumentBuilder.Companion.requireOnTimeUserArgumentScoped(
    onTimeUserRepository: OnTimeUserRepository,
    argumentName: String = "user",
    command: suspend CommandExecutionScope<CommandSource>.(onTimeUser: OnTimeUser) -> Unit
): RequiredArgumentBuilder<CommandSource, String> =
    required<CommandSource, String>(argumentName, StringArgumentType.SingleWord)
        .suggestOnTimeUserArgument(onTimeUserRepository)
        .executesScoped { command(extractOnTimeUserArgument(onTimeUserRepository, argumentName)) }

fun RequiredArgumentBuilder<CommandSource, String>.suggestOnTimeUserArgument(
    onTimeUserRepository: OnTimeUserRepository
): RequiredArgumentBuilder<CommandSource, String> =
    suggestsScoped { onTimeUserRepository.getAllUsernames(startWith = remainingLowerCase).suggestAll() }

@CommandContextScopeDsl
suspend fun CommandExecutionScope<CommandSource>.extractOnTimeUserArgument(
    onTimeUserRepository: OnTimeUserRepository,
    argumentName: String = "user"
): OnTimeUser {
    val username = context.get<String>(argumentName)
    val user = onTimeUserRepository.getByUsername(username)
    if (user == null) {
        context.source.sendMessage(
            Component.text()
                .append(PluginMessages.pluginPrefix)
                .append(Component.text("User with name ", NamedTextColor.RED))
                .append(Component.text(username, NamedTextColor.GOLD))
                .append(Component.text(" not found!", NamedTextColor.RED))
                .build()
        )
        yieldError()
    }
    return user
}

@CommandContextScopeDsl
suspend fun CommandExecutionScope<CommandSource>.extractOnTimeUserSource(
    onTimeUserRepository: OnTimeUserRepository,
    playerProvider: suspend () -> Player = { extractPlayerSource() }
): OnTimeUser {
    val player = playerProvider()
    val user = onTimeUserRepository.getById(player.id)
    if (user == null) {
        context.source.sendMessage(
            Component.text()
                .append(PluginMessages.pluginPrefix)
                .append(Component.text("User with name ", NamedTextColor.RED))
                .append(Component.text(player.username, NamedTextColor.GOLD))
                .append(Component.text(" not found!", NamedTextColor.RED))
                .build()
        )
        yieldError()
    }
    return user
}
