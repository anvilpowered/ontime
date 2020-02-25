/*
 *   OnTime - AnvilPowered
 *   Copyright (C) 2020 Cableguy20
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.ontime.sponge.plugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.ontime.api.tasks.SyncTaskService;
import org.anvilpowered.ontime.common.plugin.OnTime;
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo;
import org.anvilpowered.ontime.sponge.commands.OnTimeCommandManager;
import org.anvilpowered.ontime.sponge.listeners.PlayerListener;
import org.anvilpowered.ontime.sponge.module.SpongeModule;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(
    id = OnTimePluginInfo.id,
    name = OnTimePluginInfo.name,
    version = OnTimePluginInfo.version,
    dependencies = @Dependency(id = "anvil"),
    description = OnTimePluginInfo.description,
    url = OnTimePluginInfo.url,
    authors = {"Cableguy20", "STG_Allen"}
)
public class OnTimeSponge extends OnTime<PluginContainer> {

    @Inject
    public OnTimeSponge(Injector injector) {
        super(injector, new SpongeModule(), SyncTaskService.class, OnTimeCommandManager.class);
    }

    @Override
    protected void applyToBuilder(Environment.Builder builder) {
        builder.addEarlyServices(PlayerListener.class, t ->
            Sponge.getEventManager().registerListeners(this, t));
    }
}
