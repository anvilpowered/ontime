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

package org.anvilpowered.ontime.velocity.task;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import net.kyori.adventure.text.TextComponent;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.ontime.luckperms.task.LuckPermsSyncTaskService;

import java.util.concurrent.TimeUnit;

public class VelocitySyncTaskService
    extends LuckPermsSyncTaskService<Player, Player, TextComponent> {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private PluginContainer pluginContainer;

    private ScheduledTask task;

    @Inject
    public VelocitySyncTaskService(Registry registry) {
        super(registry);
    }

    @Override
    public void startSyncTask() {
        task = proxyServer.getScheduler()
            .buildTask(pluginContainer, getSyncTask())
            .repeat(1, TimeUnit.MINUTES)
            .schedule();
    }

    @Override
    public void stopSyncTask() {
        if (task != null) {
            task.cancel();
        }
    }
}
