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
import org.anvilpowered.anvil.core.user.hasPermissionSet
import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.builder.executesSingleSuccess
import org.anvilpowered.kbrig.context.executesScoped
import org.anvilpowered.kbrig.tree.LiteralCommandNode
import org.anvilpowered.ontime.core.user.getBasicInfo
import org.anvilpowered.ontime.core.user.getExtendedInfo

private val checkBaseUsage: Component = Component.text("Usage: /ontime check")
private val checkAdminUsage: Component = Component.text("Usage: /ontime check [<user>]")

fun OnTimeCommandFactory.createCheck(): LiteralCommandNode<CommandSource> =
    ArgumentBuilder.literal<CommandSource>("check")
        .requires { source ->
            source.hasPermissionSet(registry[onTimeKeys.PERMISSION_USER_CHECK]) ||
                source.hasPermissionSet(registry[onTimeKeys.PERMISSION_ADMIN_CHECK])
        }
        .executesScoped {
            val user = extractOnTimeUserSource(onTimeUserRepository)
            if (context.source.hasPermissionSet(registry[onTimeKeys.PERMISSION_ADMIN_CHECK])) {
                context.source.sendMessage(user.getExtendedInfo())
            } else {
                context.source.sendMessage(user.getBasicInfo())
            }
        }
        .then(
            ArgumentBuilder.literal<CommandSource>("help")
                .executesSingleSuccess { context ->
                    if (context.source.hasPermissionSet(registry[onTimeKeys.PERMISSION_ADMIN_CHECK])) {
                        context.source.sendMessage(checkAdminUsage)
                    } else {
                        context.source.sendMessage(checkBaseUsage)
                    }
                },
        )
        .then(
            ArgumentBuilder.requireOnTimeUserArgumentScoped(onTimeUserRepository) { user ->
                context.source.sendMessage(user.getExtendedInfo())
            },
        )
        .build()
