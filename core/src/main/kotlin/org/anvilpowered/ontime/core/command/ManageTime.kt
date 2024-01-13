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
import org.anvilpowered.anvil.core.command.CommandSource
import org.anvilpowered.anvil.core.command.extractDurationArgument
import org.anvilpowered.anvil.core.user.requiresPermission
import org.anvilpowered.kbrig.argument.StringArgumentType
import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.builder.LiteralArgumentBuilder
import org.anvilpowered.kbrig.builder.executesFailure
import org.anvilpowered.kbrig.context.executesScoped
import org.anvilpowered.kbrig.context.get
import org.anvilpowered.ontime.api.PluginMessages
import org.anvilpowered.ontime.api.user.OnTimeUserRepository
import java.time.Duration

context(OnTimeCommandFactory)
fun LiteralArgumentBuilder<CommandSource>.thenSubCommand(
    name: String,
    dbFun: suspend OnTimeUserRepository.(username: String, duration: Long) -> Boolean,
    successMessage: (username: String, duration: Duration) -> Component,
): LiteralArgumentBuilder<CommandSource> {
    val usage = PluginMessages.getCommandUsage("ontime $name <user> <time>")
    val node = ArgumentBuilder.literal<CommandSource>(name)
        .requiresPermission(registry[onTimeKeys.PERMISSION_ADMIN_EDIT])
        .addDefaultUsage(usage)
        .then(
            ArgumentBuilder.required<CommandSource, String>("user", StringArgumentType.SingleWord)
                .suggestOnTimeUserArgument(onTimeUserRepository)
                .executesFailure { context -> context.source.sendMessage(usage) }
                .then(
                    ArgumentBuilder.required<CommandSource, String>("duration", StringArgumentType.GreedyPhrase)
                        .executesScoped {
                            val username = context.get<String>("user")
                            val duration = extractDurationArgument()
                            if (onTimeUserRepository.dbFun(username, duration.seconds)) {
                                context.source.sendMessage(successMessage(username, duration))
                            } else {
                                context.source.sendMessage(PluginMessages.getNoSuchUser(username))
                            }
                        },
                ),

        ).build()
    return then(node)
}
