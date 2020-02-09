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

package org.anvilpowered.ontime.sponge.module;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.anvilpowered.ontime.api.tasks.SyncTaskService;
import org.anvilpowered.ontime.common.data.config.MSOnTimeConfigurationService;
import org.anvilpowered.ontime.common.module.CommonModule;
import org.anvilpowered.ontime.sponge.data.config.MSOnTimeSpongeConfigurationService;
import org.anvilpowered.ontime.sponge.tasks.SpongeSyncTaskService;

public class SpongeModule extends CommonModule<User, Player, Text, CommandSource> {

    @Override
    protected void configure() {
        super.configure();

        bind(MSOnTimeConfigurationService.class).to(MSOnTimeSpongeConfigurationService.class);

        bind(SyncTaskService.class).to(SpongeSyncTaskService.class);
    }
}
