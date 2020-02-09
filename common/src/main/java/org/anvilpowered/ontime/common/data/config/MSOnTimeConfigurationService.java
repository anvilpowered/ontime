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

package org.anvilpowered.ontime.common.data.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.anvilpowered.ontime.api.data.key.MSOnTimeKeys;
import org.anvilpowered.anvil.api.data.key.Keys;
import org.anvilpowered.anvil.common.data.config.CommonConfigurationService;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class MSOnTimeConfigurationService extends CommonConfigurationService {

    @Inject
    public MSOnTimeConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);

        Map<String, Integer> defaultRankMap = new HashMap<>();
        defaultRankMap.put("player", 5);
        defaultRankMap.put("member", 60);

        defaultMap.put(MSOnTimeKeys.RANKS, defaultRankMap);
        defaultMap.put(Keys.MONGODB_DBNAME, "msontime");
    }

    @Override
    protected void initNodeNameMap() {
        super.initNodeNameMap();
        nodeNameMap.put(MSOnTimeKeys.RANKS, "ranks");
    }

    @Override
    protected void initNodeDescriptionMap() {
        super.initNodeDescriptionMap();
        nodeDescriptionMap.put(MSOnTimeKeys.RANKS, "\nPlayer ranks and their time requirement in minutes.");
    }
}
