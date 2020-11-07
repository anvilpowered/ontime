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

import org.anvilpowered.anvil.api.Environment
import org.anvilpowered.anvil.api.command.CommandNode
import org.anvilpowered.anvil.api.command.CommandService
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo
import java.util.function.Function
import java.util.function.Predicate
import javax.inject.Inject

abstract class CommonOnTimeCommandNode<TCommandExecutor, TCommandSource> protected constructor(
    protected var registry: Registry
) : CommandNode<TCommandSource> {
    companion object {
        val ADD_ALIAS = listOf("add", "a")
        val CHECK_ALIAS = listOf("check", "c", "info", "i")
        val IMPORT_ALIAS = listOf("import")
        val SET_BONUS_ALIAS = listOf("setbonus", "sb")
        val SET_TOTAL_ALIAS = listOf("set", "s", "settotal", "st")
        val HELP_ALIAS = listOf("help", "h")
        val VERSION_ALIAS = listOf("version", "v")

        const val ADD_DESCRIPTION = "Add bonus time to a player"
        const val CHECK_DESCRIPTION = "Check playtime"
        const val IMPORT_DESCRIPTION = "Import data from rankupper"
        const val SET_BONUS_DESCRIPTION = "Set bonus playtime"
        const val SET_TOTAL_DESCRIPTION = "Set total playtime"
        const val HELP_DESCRIPTION = "Shows this help page."
        const val VERSION_DESCRIPTION = "Shows the plugin version"

        const val ROOT_DESCRIPTION = "${OnTimePluginInfo.name} root command"
        const val ADD_USAGE = "<user> <time>"
        const val CHECK_USAGE = "[<user>]"
        const val IMPORT_USAGE = "<path>"
        const val SET_BONUS_USAGE = "<user> <time>"
        const val SET_TOTAL_USAGE = "<user> <time>"
        const val HELP_COMMAND = "/ontime help"
    }

    private var alreadyLoaded: Boolean
    private var descriptions: MutableMap<List<String>, Function<TCommandSource, String>>
    private var permissions: Map<List<String>, Predicate<TCommandSource>>

    private var usages: MutableMap<List<String>, Function<TCommandSource, String>>

    @Inject
    protected lateinit var commandService: CommandService<TCommandExecutor, TCommandSource>

    @Inject
    protected lateinit var environment: Environment

    init {
        alreadyLoaded = false
        registry.whenLoaded {
            if (alreadyLoaded) return@whenLoaded
            loadCommands()
            alreadyLoaded = true
        }.register()
        descriptions = mutableMapOf()
        permissions = mutableMapOf()
        usages = mutableMapOf()
        descriptions.put(ADD_ALIAS) { ADD_DESCRIPTION }
        descriptions.put(CHECK_ALIAS) { CHECK_DESCRIPTION }
        descriptions.put(IMPORT_ALIAS) { IMPORT_DESCRIPTION }
        descriptions.put(SET_BONUS_ALIAS) { SET_BONUS_DESCRIPTION }
        descriptions.put(SET_TOTAL_ALIAS) { SET_TOTAL_DESCRIPTION }
        descriptions.put(HELP_ALIAS) { HELP_DESCRIPTION }
        descriptions.put(VERSION_ALIAS) { VERSION_DESCRIPTION }
        usages.put(ADD_ALIAS) { ADD_USAGE }
        usages.put(CHECK_ALIAS) { CHECK_USAGE }
        usages.put(IMPORT_ALIAS) { IMPORT_USAGE }
        usages.put(SET_BONUS_ALIAS) { SET_BONUS_USAGE }
        usages.put(SET_TOTAL_ALIAS) { SET_TOTAL_USAGE }
    }

    protected abstract fun loadCommands()
    override fun getDescriptions(): Map<List<String>, Function<TCommandSource, String>> = descriptions
    override fun getPermissions(): Map<List<String>, Predicate<TCommandSource>> = permissions
    override fun getUsages(): Map<List<String>, Function<TCommandSource, String>> = usages
    override fun getName(): String = OnTimePluginInfo.id
}
