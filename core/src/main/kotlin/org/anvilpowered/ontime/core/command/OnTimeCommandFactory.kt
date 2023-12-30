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
import org.anvilpowered.anvil.core.command.config.ConfigCommandFactory
import org.anvilpowered.anvil.core.command.format
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.builder.executesSingleSuccess
import org.anvilpowered.kbrig.tree.LiteralCommandNode
import org.anvilpowered.ontime.api.PluginMessages
import org.anvilpowered.ontime.api.config.OnTimeKeys
import org.anvilpowered.ontime.api.user.OnTimeUserRepository

class OnTimeCommandFactory(
    val registry: Registry,
    val onTimeKeys: OnTimeKeys,
    val onTimeUserRepository: OnTimeUserRepository,
    private val configCommandFactory: ConfigCommandFactory,
) {
    fun create(): LiteralCommandNode<CommandSource> {
        return ArgumentBuilder.literal<CommandSource>("ontime")
            .executesSingleSuccess { context -> context.source.sendMessage(PluginMessages.pluginHome) }
            .thenSubCommand("set", OnTimeUserRepository::setTotalTime) { username, duration ->
                Component.text()
                    .append(PluginMessages.pluginPrefix)
                    .append(Component.text("Successfully set total time to ", NamedTextColor.GREEN))
                    .append(Component.text(duration.format(), NamedTextColor.AQUA))
                    .append(Component.text(" for ", NamedTextColor.GREEN))
                    .append(Component.text(username, NamedTextColor.GOLD))
                    .build()
            }
            .thenSubCommand("add", OnTimeUserRepository::addBonusTime) { username, duration ->
                Component.text()
                    .append(PluginMessages.pluginPrefix)
                    .append(Component.text("Successfully added ", NamedTextColor.GREEN))
                    .append(Component.text(duration.format(), NamedTextColor.AQUA))
                    .append(Component.text(" bonus time to ", NamedTextColor.GREEN))
                    .append(Component.text(username, NamedTextColor.GOLD))
                    .build()
            }
            .thenSubCommand("setbonus", OnTimeUserRepository::setBonusTime) { username, duration ->
                Component.text()
                    .append(PluginMessages.pluginPrefix)
                    .append(Component.text("Successfully set bonus time to ", NamedTextColor.GREEN))
                    .append(Component.text(duration.format(), NamedTextColor.AQUA))
                    .append(Component.text(" for ", NamedTextColor.GREEN))
                    .append(Component.text(username, NamedTextColor.GOLD))
                    .build()
            }
            .then(createCheck())
            .then(configCommandFactory.create())
            .build()
    }
}
