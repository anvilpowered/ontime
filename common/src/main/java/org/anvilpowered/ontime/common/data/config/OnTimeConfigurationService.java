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
import org.anvilpowered.anvil.api.data.key.Keys;
import org.anvilpowered.anvil.base.data.config.BaseConfigurationService;
import org.anvilpowered.ontime.api.data.key.OnTimeKeys;
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class OnTimeConfigurationService extends BaseConfigurationService {

    @Inject
    public OnTimeConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
        defaultMap.put(Keys.DATA_DIRECTORY, OnTimePluginInfo.id);
        defaultMap.put(Keys.MONGODB_DBNAME, OnTimePluginInfo.id);

        Map<String, Integer> defaultRankMap = new HashMap<>();
        defaultRankMap.put("player", 600);
        defaultRankMap.put("member", 1800);

        defaultMap.put(OnTimeKeys.RANKS, defaultRankMap);
    }

    @Override
    protected void initNodeNameMap() {
        super.initNodeNameMap();
        nodeNameMap.put(OnTimeKeys.RANKS, "ranks");
    }

    @Override
    protected void initNodeDescriptionMap() {
        super.initNodeDescriptionMap();
        nodeDescriptionMap.put(OnTimeKeys.RANKS, "\nPlayer ranks and their time requirement in seconds.");
    }
}
