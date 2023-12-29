package org.anvilpowered.ontime.api

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

object PluginMessages {
    val pluginPrefix = Component.text("[OnTime] ", NamedTextColor.GOLD, TextDecoration.BOLD)

    val pluginHome = Component.text()
        .append(pluginPrefix)
        .append(Component.text("Running version 0.4.0-SNAPSHOT", NamedTextColor.AQUA)) // TODO: Read from file
        .append(Component.newline())
        .append(Component.text("Use ", NamedTextColor.GRAY))
        .append(Component.text("/ontime help", NamedTextColor.GOLD))
        .append(Component.text(" for a list of commands.", NamedTextColor.GRAY))
        .build()

    val notEnoughArgs = Component.text("Not enough arguments!", NamedTextColor.RED)
    val noPermission = Component.text("Insufficient Permissions!", NamedTextColor.RED)

    fun getCommandUsage(usage: String) = Component.text()
        .append(pluginPrefix)
        .append(Component.text("Command Usage", NamedTextColor.RED, TextDecoration.BOLD))
        .append(Component.newline())
        .append(Component.text(usage, NamedTextColor.AQUA))
        .build()

    fun getNoSuchUser(username: String) = Component.text()
        .append(pluginPrefix)
        .append(Component.text("User with name ", NamedTextColor.RED))
        .append(Component.text(username, NamedTextColor.GOLD))
        .append(Component.text(" not found!", NamedTextColor.RED))
        .build()
}
