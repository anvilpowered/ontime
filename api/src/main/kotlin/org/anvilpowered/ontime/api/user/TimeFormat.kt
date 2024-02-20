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

package org.anvilpowered.ontime.api.user

import org.anvilpowered.anvil.core.command.format
import java.time.Duration
import java.time.format.DateTimeFormatter

val OnTimeUser.createdUtcFormatted: String
    get() = createdUtc.format(DateTimeFormatter.ISO_DATE_TIME)

val OnTimeUser.playTimeFormatted: String
    get() = Duration.ofSeconds(playTime).format()

val OnTimeUser.bonusTimeFormatted: String
    get() = Duration.ofSeconds(bonusTime).format()

val OnTimeUser.totalTimeFormatted: String
    get() = Duration.ofSeconds(totalTime).format()
