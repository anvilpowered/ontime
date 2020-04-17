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
public class CommonConfigurationService extends BaseConfigurationService {

    @Inject
    public CommonConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
        withMongoDB();
        setDefault(Keys.DATA_DIRECTORY, OnTimePluginInfo.id);
        setDefault(Keys.MONGODB_DBNAME, OnTimePluginInfo.id);

        Map<String, Integer> defaultRankMap = new HashMap<>();
        defaultRankMap.put("noob", 0);
        defaultRankMap.put("player", 600);
        defaultRankMap.put("trusted", 1800);
        setDefault(OnTimeKeys.RANKS, defaultRankMap);
        setName(OnTimeKeys.RANKS, "ranks");
        setDescription(OnTimeKeys.RANKS, "\nPlayer ranks and their time requirement in seconds.");
    }
}
