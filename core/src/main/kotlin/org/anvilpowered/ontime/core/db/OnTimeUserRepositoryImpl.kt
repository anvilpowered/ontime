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

package org.anvilpowered.ontime.core.db

import org.anvilpowered.anvil.core.db.MutableRepository
import org.anvilpowered.anvil.core.db.SizedIterable
import org.anvilpowered.ontime.api.user.OnTimeUser
import org.anvilpowered.ontime.api.user.OnTimeUserRepository
import org.anvilpowered.ontime.api.user.bonusTimeFormatted
import org.anvilpowered.ontime.api.user.playTimeFormatted
import org.anvilpowered.ontime.api.user.totalTimeFormatted
import org.apache.logging.log4j.Logger
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.mapLazy
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

class OnTimeUserRepositoryImpl(
    private val logger: Logger,
) : OnTimeUserRepository {
    override suspend fun create(item: OnTimeUser.CreateDto): OnTimeUser = newSuspendedTransaction {
        val user = DBOnTimeUser.new(item.id) {
            username = item.username
            createdUtc = LocalDateTime.now(ZoneId.of("UTC"))
            playTime = 0
            bonusTime = 0
        }
        logger.info("Created new OnTimeUser ${user.id.value} with data $item")
        user
    }

    override suspend fun put(item: OnTimeUser.CreateDto): MutableRepository.PutResult<OnTimeUser> = newSuspendedTransaction {
        val existingUser = getById(item.id)
        if (existingUser == null) {
            // create a new OnTimeUser
            val newUser = DBOnTimeUser.new(item.id) {
                username = item.username
                createdUtc = LocalDateTime.now(ZoneId.of("UTC"))
                playTime = 0
                bonusTime = 0
            }
            logger.info("Created new OnTimeUser ${item.username} (${item.id}) with data $item")
            MutableRepository.PutResult(newUser, created = true)
        } else {
            logger.info(
                "Loaded existing OnTime user ${existingUser.username} (${existingUser.uuid}) with" +
                    "play time ${existingUser.playTimeFormatted}, " +
                    "bonus time ${existingUser.bonusTimeFormatted}, " +
                    "and total time ${existingUser.totalTimeFormatted}",
            )
            MutableRepository.PutResult(existingUser, created = false)
        }
    }

    override suspend fun getAllUsernames(contains: String): SizedIterable<String> = newSuspendedTransaction {
        DBOnTimeUser.find { OnTimeUsers.username.lowerCase() like "%${contains.lowercase()}%" }.mapLazy { it.username }
    }.wrap()

    override suspend fun getByUsername(username: String): OnTimeUser? = newSuspendedTransaction {
        DBOnTimeUser.find { OnTimeUsers.username eq username }.firstOrNull()
    }

    private fun DBOnTimeUser.addTime(time: Long) {
        playTime += time
        logger.debug("Added {} seconds to User {}", time, id.value)
    }

    override suspend fun addPlayTime(id: UUID, duration: Long): Long? = newSuspendedTransaction {
        DBOnTimeUser.findById(id)?.let { user ->
            user.addTime(duration)
            user.playTime + user.bonusTime
        }
    }

    private fun DBOnTimeUser.setTime(time: Long) {
        bonusTime = time - playTime
        logger.info("Set total time of user $username (${id.value}) to $time")
    }

    override suspend fun setTotalTime(id: UUID, duration: Long): Boolean = newSuspendedTransaction {
        DBOnTimeUser.findById(id)?.setTime(duration) != null
    }

    override suspend fun setTotalTime(username: String, duration: Long) = newSuspendedTransaction {
        DBOnTimeUser.find { OnTimeUsers.username eq username }.firstOrNull()?.setTime(duration) != null
    }

    private fun DBOnTimeUser.addBonusTime(time: Long) {
        bonusTime += time
        logger.info("Added $time seconds to user $username (${id.value})")
    }

    override suspend fun addBonusTime(id: UUID, duration: Long): Boolean = newSuspendedTransaction {
        DBOnTimeUser.findById(id)?.addBonusTime(duration) != null
    }

    override suspend fun addBonusTime(username: String, duration: Long): Boolean = newSuspendedTransaction {
        DBOnTimeUser.find { OnTimeUsers.username eq username }.firstOrNull()?.addBonusTime(duration) != null
    }

    private fun DBOnTimeUser.setBonusTime(time: Long) {
        bonusTime = time
        logger.info("Set bonus time of user $username (${id.value}) to $time")
    }

    override suspend fun setBonusTime(id: UUID, duration: Long): Boolean = newSuspendedTransaction {
        DBOnTimeUser.findById(id)?.setBonusTime(duration) != null
    }

    override suspend fun setBonusTime(username: String, duration: Long): Boolean = newSuspendedTransaction {
        DBOnTimeUser.find { OnTimeUsers.username eq username }.firstOrNull()?.setBonusTime(duration) != null
    }

    override suspend fun getById(id: UUID): OnTimeUser? = newSuspendedTransaction {
        DBOnTimeUser.findById(id)
    }

    override suspend fun countAll(): Long = newSuspendedTransaction {
        DBOnTimeUser.all().count()
    }

    override suspend fun exists(id: UUID): Boolean = newSuspendedTransaction {
        DBOnTimeUser.findById(id) != null
    }

    override suspend fun deleteById(id: UUID): Boolean = newSuspendedTransaction {
        OnTimeUsers.deleteWhere { OnTimeUsers.id eq id } > 0
    }
}
