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

package org.anvilpowered.ontime.core

import kotlinx.coroutines.runBlocking
import org.anvilpowered.anvil.core.command.CommandSource
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.kbrig.tree.LiteralCommandNode
import org.anvilpowered.ontime.api.config.OnTimeKeys
import org.anvilpowered.ontime.core.command.OnTimeCommandFactory
import org.anvilpowered.ontime.core.db.OnTimeUsers
import org.anvilpowered.ontime.core.registrar.Registrar
import org.anvilpowered.ontime.core.task.LuckPermsSyncTaskService
import org.apache.logging.log4j.Logger
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate

class OnTimePlugin(
    private val registry: Registry,
    private val onTimeKeys: OnTimeKeys,
    private val logger: Logger,
    private val luckPermsSyncTaskService: LuckPermsSyncTaskService,
    private val onTimeCommandFactory: OnTimeCommandFactory,
    private val registrars: List<Registrar>,
) {

    private val timer = Timer("OnTimeSyncTimer", true)

    fun enable() {
        logger.info("Enabling OnTime...")
        try {
            connectDatabase()
        } catch (e: Exception) {
            logger.error("Could not connect to the database, please check your config!", e)
        }
        registerTasks()
        registrars.forEach(Registrar::register)
    }

    fun disable() {
        logger.info("Disabling OnTime...")
        timer.cancel()
        logger.info("Finished disabling OnTime.")
    }

    fun registerCommands(callback: (LiteralCommandNode<CommandSource>) -> Unit) {
        logger.info("Building command tree...")
        val command = onTimeCommandFactory.create()
        logger.info("Registering commands...")
        callback(command)
        logger.info("Finished registering commands.")
    }

    private val dbDriver = mapOf(
        "postgresql" to "org.postgresql.Driver",
        "mariadb" to "org.mariadb.jdbc.Driver",
    )

    private fun connectDatabase() {
        logger.info("Connecting to database...")
        val dbDriver = checkNotNull(dbDriver[registry[onTimeKeys.DB_TYPE]]) {
            "Unknown db type ${registry[onTimeKeys.DB_TYPE]}. Available: postgresql, mariadb."
        }
        logger.info("Using database driver $dbDriver")
        Database.connect(
            registry[onTimeKeys.DB_URL],
            driver = dbDriver,
            user = registry[onTimeKeys.DB_USER],
            password = registry[onTimeKeys.DB_PASSWORD],
        )
        logger.info("Finished connecting to database.")
        logger.info("Creating tables...")
        transaction {
            SchemaUtils.createMissingTablesAndColumns(OnTimeUsers)
        }
        logger.info("Finished creating tables.")
    }

    private fun registerTasks() {
        logger.info("Registering tasks...")
        timer.scheduleAtFixedRate(0L, 60_000L) {
            runBlocking {
                luckPermsSyncTaskService.sync()
            }
        }
        logger.info("Finished registering tasks.")
    }
}
