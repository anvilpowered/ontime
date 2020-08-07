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

package org.anvilpowered.ontime.spigot.task;

import com.google.inject.Inject;
import net.md_5.bungee.api.chat.TextComponent;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.ontime.luckperms.task.LuckPermsSyncTaskService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class SpigotSyncTaskService
    extends LuckPermsSyncTaskService<Player, Player, TextComponent> {

    @Inject
    private JavaPlugin plugin;

    private BukkitTask task;

    @Inject
    public SpigotSyncTaskService(Registry registry) {
        super(registry);
    }

    @Override
    public void startSyncTask() {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, getSyncTask(), 0, 1200);
    }

    @Override
    public void stopSyncTask() {
        if (task != null) {
            task.cancel();
        }
    }
}
