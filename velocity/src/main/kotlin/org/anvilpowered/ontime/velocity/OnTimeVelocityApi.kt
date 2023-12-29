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

package org.anvilpowered.ontime.velocity

import com.google.inject.Injector
import org.anvilpowered.anvil.core.config.KeyNamespace
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.velocity.config.configureVelocityDefaults
import org.anvilpowered.ontime.api.OnTimeApi
import org.anvilpowered.ontime.api.config.OnTimeKeys
import org.anvilpowered.ontime.api.user.OnTimeUserRepository
import org.anvilpowered.ontime.core.command.OnTimeCommandFactory
import org.anvilpowered.ontime.core.db.OnTimeUserRepositoryImpl
import org.anvilpowered.ontime.core.registrar.Registrar
import org.anvilpowered.ontime.core.task.LuckPermsSyncTaskService
import org.anvilpowered.ontime.velocity.listener.JoinListener
import org.anvilpowered.ontime.velocity.registrar.VelocityCommandRegistrar
import org.anvilpowered.ontime.velocity.registrar.VelocityListenerRegistrar
import org.apache.logging.log4j.Logger
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun OnTimeApi.Companion.createVelocity(injector: Injector, logger: Logger): OnTimeApi {
    val velocityModule = module {
        Registry.configureVelocityDefaults(injector, logger)

        singleOf(::OnTimeKeys) { bind<KeyNamespace>() }

        singleOf(::OnTimeUserRepositoryImpl) { bind<OnTimeUserRepository>() }

        singleOf(::OnTimeCommandFactory)

        singleOf(::JoinListener)
        singleOf(::LuckPermsSyncTaskService)

        singleOf(::VelocityCommandRegistrar) { bind<Registrar>() }
        singleOf(::VelocityListenerRegistrar) { bind<Registrar>() }
    }

    return object : OnTimeApi {
        override val module = velocityModule
    }
}
