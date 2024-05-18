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

import org.anvilpowered.ontime.api.user.OnTimeUser
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.UUID

internal object OnTimeUserTable : UUIDTable("ontime_users") {
    val username = varchar("username", 16).uniqueIndex()
    val createdUtc = datetime("created_utc")
    val playTime = long("play_time")
    val bonusTime = long("bonus_time")
}

internal class OnTimeUserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var username by OnTimeUserTable.username
    var createdUtc by OnTimeUserTable.createdUtc
    var playTime by OnTimeUserTable.playTime
    var bonusTime by OnTimeUserTable.bonusTime

    companion object : UUIDEntityClass<OnTimeUserEntity>(OnTimeUserTable, OnTimeUserEntity::class.java, ::OnTimeUserEntity)
}

internal fun OnTimeUserEntity.toOnTimeUser() = OnTimeUser(
    id = this.id.value,
    username = this.username,
    createdUtc = this.createdUtc,
    playTime = this.playTime,
    bonusTime = this.bonusTime,
)

internal fun ResultRow.toOnTimeUser() = OnTimeUser(
    id = this[OnTimeUserTable.id].value,
    username = this[OnTimeUserTable.username],
    createdUtc = this[OnTimeUserTable.createdUtc],
    playTime = this[OnTimeUserTable.playTime],
    bonusTime = this[OnTimeUserTable.bonusTime],
)
