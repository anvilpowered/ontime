/*
 *   OnTime - AnvilPowered
 *   Copyright (C) 2020
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.ontime.api;

import com.google.inject.Injector;
import com.google.inject.Module;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo;

public class OnTimeImpl extends OnTime {

    protected OnTimeImpl(Injector injector, Module module) {
        super(OnTimePluginInfo.id, injector, module);
    }

    @Override
    protected void applyToBuilder(Environment.Builder builder) {
        builder.withRootCommand();
    }

    @Override
    protected void whenReady(Environment environment) {
        super.whenReady(environment);
        OnTime.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
