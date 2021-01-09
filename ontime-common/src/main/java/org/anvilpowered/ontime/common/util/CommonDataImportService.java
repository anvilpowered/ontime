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

package org.anvilpowered.ontime.common.util;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.anvilpowered.ontime.api.member.MemberManager;
import org.anvilpowered.ontime.api.util.DataImportService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class CommonDataImportService<TString> implements DataImportService {

    @Inject
    MemberManager<TString> memberManager;

    @Override
    public void importRankUpData(Path dataPath) {
        try {
            ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(dataPath).build();
            CommentedConfigurationNode root = configLoader.load();

            for (Object objKey : root.getNode("playerData").getChildrenMap().keySet()) {

                String key = (String) objKey;
                String ontime = root.getNode("playerData", key, "TimePlayed").getString();

                if (ontime == null) {
                    continue;
                }
                //Insert the user into MongoDB
                memberManager.getPrimaryComponent().getOneOrGenerateForUser(UUID.fromString(key), Long.parseLong(ontime) * 60L);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importRankUpperData(Path dataPath) {
        try {
            ConfigurationLoader<CommentedConfigurationNode> configLoader =
                HoconConfigurationLoader.builder().setPath(dataPath).build();
            CommentedConfigurationNode root = configLoader.load();

            for (Object objKey : root.getChildrenMap().keySet()) {
                String key = (String) objKey;
                String ontime = root.getNode(key, "TimePlayed").getString();

                if (ontime == null) {
                    continue;
                }
                // Assuming that playtime is stored in minutes
                memberManager.getPrimaryComponent().getOneOrGenerateForUser(UUID.fromString(key), Long.parseLong(ontime) * 60L);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
