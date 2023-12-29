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

package org.anvilpowered.ontime.velocity.registrar

import com.mojang.brigadier.tree.LiteralCommandNode
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ProxyServer
import org.anvilpowered.anvil.velocity.command.toVelocity
import org.anvilpowered.ontime.core.command.OnTimeCommandFactory
import org.anvilpowered.ontime.core.registrar.Registrar
import org.apache.logging.log4j.Logger

class VelocityCommandRegistrar(
    private val proxyServer: ProxyServer,
    private val logger: Logger,
    private val onTimeCommandFactory: OnTimeCommandFactory,
) : Registrar {
    private fun LiteralCommandNode<CommandSource>.register() = proxyServer.commandManager.register(BrigadierCommand(this))

    override fun register() {
        logger.info("Building command trees and registering commands...")
        onTimeCommandFactory.create().toVelocity().register()
        logger.info("Finished registering commands.")
    }
}
