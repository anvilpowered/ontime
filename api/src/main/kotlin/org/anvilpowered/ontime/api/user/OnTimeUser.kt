package org.anvilpowered.ontime.api.user

import org.anvilpowered.anvil.core.db.Creates
import org.anvilpowered.anvil.core.db.DomainEntity
import java.time.LocalDateTime
import java.util.UUID

interface OnTimeUser : DomainEntity {
    val username: String
    val createdUtc: LocalDateTime
    val playTime: Long
    val bonusTime: Long

    data class CreateDto(
        val id: UUID,
        val username: String,
    ) : Creates<OnTimeUser>
}

val OnTimeUser.totalTime: Long
    get() = playTime + bonusTime
