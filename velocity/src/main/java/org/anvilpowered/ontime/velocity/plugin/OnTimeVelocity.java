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

package org.anvilpowered.ontime.velocity.plugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.ontime.common.plugin.OnTime;
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo;
import org.anvilpowered.ontime.velocity.listener.VelocityPlayerListener;
import org.anvilpowered.ontime.velocity.module.VelocityModule;

@Plugin(
    id = OnTimePluginInfo.id,
    name = OnTimePluginInfo.name,
    version = OnTimePluginInfo.version,
    dependencies = @Dependency(id = "anvil"),
    description = OnTimePluginInfo.description,
    url = OnTimePluginInfo.url,
    authors = {"Cableguy20", "STG_Allen"}
)
public class OnTimeVelocity extends OnTime<PluginContainer> {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    public OnTimeVelocity(Injector injector) {
        super(injector, new VelocityModule());
    }

    @Override
    protected void applyToBuilder(Environment.Builder builder) {
        super.applyToBuilder(builder);
        builder.addEarlyServices(VelocityPlayerListener.class, t ->
            proxyServer.getEventManager().register(this, t));
    }
}
