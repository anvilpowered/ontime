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

import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.config.configureDefaults
import org.anvilpowered.anvil.velocity.AnvilVelocityApi
import org.anvilpowered.ontime.api.OnTimeApi
import org.anvilpowered.ontime.core.coreModule
import org.anvilpowered.ontime.core.registrar.Registrar
import org.anvilpowered.ontime.velocity.listener.VelocityJoinListener
import org.anvilpowered.ontime.velocity.registrar.VelocityListenerRegistrar
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun OnTimeApi.Companion.createVelocity(anvil: AnvilVelocityApi): OnTimeApi {
    val velocityModule = module {
        includes(coreModule)
        includes(anvil.module)
        Registry.configureDefaults(anvil)
        singleOf(::VelocityJoinListener)
        singleOf(::VelocityListenerRegistrar) { bind<Registrar>() }
    }

    return object : OnTimeApi {
        override val module = velocityModule
    }
}
