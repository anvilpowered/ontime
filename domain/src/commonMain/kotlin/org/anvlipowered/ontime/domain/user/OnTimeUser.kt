package org.anvlipowered.ontime.domain.user

import org.sourcegrade.kontour.DomainEntity
import org.sourcegrade.kontour.UUID

data class OnTimeUser(
    override val id: UUID,
    val playTime: Long,
    val bonusTime: Long,
) : DomainEntity
