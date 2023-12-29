package org.anvilpowered.ontime.api.user

import org.anvilpowered.anvil.core.db.MutableRepository
import org.jetbrains.exposed.sql.SizedIterable
import java.util.UUID

interface OnTimeUserRepository : MutableRepository<OnTimeUser, OnTimeUser.CreateDto> {

    suspend fun getAllUsernames(startWith: String = ""): SizedIterable<String>

    suspend fun getByUsername(username: String): OnTimeUser?

    /**
     * @return the new total play time of the user in seconds, or null if the user was not found
     */
    suspend fun addPlayTime(id: UUID, duration: Long): Long?

    suspend fun setTotalTime(id: UUID, duration: Long): Boolean

    suspend fun setTotalTime(username: String, duration: Long): Boolean

    suspend fun addBonusTime(id: UUID, duration: Long): Boolean

    suspend fun addBonusTime(username: String, duration: Long): Boolean

    suspend fun setBonusTime(id: UUID, duration: Long): Boolean

    suspend fun setBonusTime(username: String, duration: Long): Boolean
}
