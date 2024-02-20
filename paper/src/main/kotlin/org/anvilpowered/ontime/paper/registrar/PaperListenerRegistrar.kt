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

package org.anvilpowered.ontime.paper.registrar

import org.anvilpowered.ontime.core.registrar.Registrar
import org.anvilpowered.ontime.paper.listener.PaperJoinListener
import org.apache.logging.log4j.Logger
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class PaperListenerRegistrar(
    private val logger: Logger,
    private val plugin: JavaPlugin,
    private val joinListener: PaperJoinListener,
) : Registrar {

    override fun register() {
        logger.info("Registering listeners...")
        Bukkit.getPluginManager().registerEvents(joinListener, plugin)
        logger.info("Finished registering listeners.")
    }
}
