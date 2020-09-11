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

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.base.plugin.BasePlugin;
import org.anvilpowered.ontime.api.member.MemberManager;
import org.anvilpowered.ontime.api.task.SyncTaskService;

public class OnTime extends BasePlugin {

    protected static Environment environment;
    private static final String NOT_LOADED = "OnTime has not been loaded yet!";

    OnTime(String name, Injector injector, Module module) {
        super(name, injector, module, SyncTaskService.class);
    }

    public static Environment getEnvironment() {
        return Preconditions.checkNotNull(environment, NOT_LOADED);
    }

    public static Registry getRegistry() {
        return getEnvironment().getInjector().getInstance(Registry.class);
    }

    public static <TString> MemberManager<TString> getMemberManager() {
        return getEnvironment().getInstance(MemberManager.class.getCanonicalName());
    }
}
