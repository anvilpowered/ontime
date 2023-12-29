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

package org.anvilpowered.ontime.core.user

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.anvilpowered.ontime.api.PluginMessages
import org.anvilpowered.ontime.api.user.OnTimeUser
import org.anvilpowered.ontime.api.user.bonusTimeFormatted
import org.anvilpowered.ontime.api.user.createdUtcFormatted
import org.anvilpowered.ontime.api.user.playTimeFormatted
import org.anvilpowered.ontime.api.user.totalTimeFormatted

// TODO: Group info

fun OnTimeUser.getBasicInfo(): Component =
    Component.text()
        .append(getHeader())
        .append(Component.text("Joined: ", NamedTextColor.GRAY))
        .append(Component.text(createdUtcFormatted, NamedTextColor.AQUA))
        .append(Component.text("Total time: ", NamedTextColor.GRAY))
        .append(Component.text(totalTimeFormatted, NamedTextColor.AQUA))
        .append(getFooter())
        .build()

fun OnTimeUser.getExtendedInfo(): Component =
    Component.text()
        .append(getHeader())
        .append(Component.text("Joined: ", NamedTextColor.GRAY))
        .append(Component.text(createdUtcFormatted, NamedTextColor.AQUA))
        .append(Component.newline())
        .append(Component.text("Play time: ", NamedTextColor.GRAY))
        .append(Component.text(playTimeFormatted, NamedTextColor.AQUA))
        .append(Component.newline())
        .append(Component.text("Bonus time: ", NamedTextColor.GRAY))
        .append(Component.text(bonusTimeFormatted, NamedTextColor.AQUA))
        .append(Component.newline())
        .append(Component.text("Total time: ", NamedTextColor.GRAY))
        .append(Component.text(totalTimeFormatted, NamedTextColor.AQUA))
        .append(getFooter())
        .build()

private fun OnTimeUser.getHeader(): Component =
    Component.text()
        .append(Component.text("========== ", NamedTextColor.DARK_GRAY))
        .append(Component.text(username, NamedTextColor.AQUA, TextDecoration.BOLD))
        .append(Component.text(" ==========", NamedTextColor.DARK_GRAY))
        .append(Component.newline())
        .append(Component.newline())
        .build()

private fun OnTimeUser.getFooter(): Component =
    Component.text()
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.text("========== ", NamedTextColor.DARK_GRAY))
        .append(PluginMessages.pluginPrefix)
        .append(Component.text("==========", NamedTextColor.DARK_GRAY))
        .build()
