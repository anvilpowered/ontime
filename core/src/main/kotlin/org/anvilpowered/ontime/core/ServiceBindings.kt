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

import org.anvilpowered.anvil.core.config.KeyNamespace
import org.anvilpowered.ontime.api.config.OnTimeKeys
import org.anvilpowered.ontime.api.user.OnTimeUserRepository
import org.anvilpowered.ontime.core.command.OnTimeCommandFactory
import org.anvilpowered.ontime.core.db.OnTimeUserRepositoryImpl
import org.anvilpowered.ontime.core.task.LuckPermsSyncTaskService
import org.anvilpowered.ontime.core.task.RankCommandService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::OnTimeKeys) { bind<KeyNamespace>() }
    singleOf(::OnTimeUserRepositoryImpl) { bind<OnTimeUserRepository>() }
    singleOf(::OnTimeCommandFactory)
    singleOf(::LuckPermsSyncTaskService)
    singleOf(::RankCommandService)
    single { OnTimePlugin(get(), get(), get(), get(), get(), getAll()) }
}
