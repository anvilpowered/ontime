package org.anvlipowered.ontime.domain.user

import org.sourcegrade.kontour.UUID

interface TimeManipulator {

    suspend fun addBonusTime(userId: UUID, time: Long)

    suspend fun setBonusTime(userId: UUID, time: Long)

    suspend fun setTotalTime(userId: UUID, time: Long)
}
