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
@file:Suppress("UnstableApiUsage")

package org.anvilpowered.ontime.paper

import io.papermc.paper.event.server.ServerResourcesLoadEvent
import org.anvilpowered.anvil.core.AnvilApi
import org.anvilpowered.anvil.paper.command.toPaper
import org.anvilpowered.anvil.paper.createPaper
import org.anvilpowered.ontime.api.OnTimeApi
import org.anvilpowered.ontime.core.OnTimePlugin
import org.bukkit.event.EventHandler
import org.bukkit.plugin.java.JavaPlugin
import org.koin.dsl.koinApplication

@Suppress("unused")
class OnTimePaperBootstrap : JavaPlugin() {

    private lateinit var plugin: OnTimePlugin

    override fun onEnable() {
        val anvil = AnvilApi.createPaper(this)
        plugin = koinApplication { modules(OnTimeApi.createPaper(anvil).module) }.koin.get()
        plugin.enable()
    }

    override fun onDisable() = plugin.disable()

    @EventHandler
    fun load(event: ServerResourcesLoadEvent) {
        plugin.registerCommands { command ->
            event.commands.register(pluginMeta, command.toPaper())
        }
    }
}
