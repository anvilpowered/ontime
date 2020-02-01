/*
 *     MSOnTime - MilSpecSG
 *     Copyright (C) 2019 Cableguy20
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

package rocks.milspecsg.msontime.common.util;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msontime.api.member.MemberManager;
import rocks.milspecsg.msontime.api.util.DataImportService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class CommonDataImportService<TString> implements DataImportService {

    @Inject
    MemberManager<TString> memberManager;

    @Override
    public void importData(Path dataPath) {
        try {
            ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(dataPath).build();
            CommentedConfigurationNode root = configLoader.load();

            for (Object objKey : root.getNode("playerData").getChildrenMap().keySet()) {

                String key = (String) objKey;
                String ontime = root.getNode("playerData", key, "TimePlayed").getString();
                String joinDate = root.getNode("playerData", key, "JoinDate").getString();

                if (ontime == null) {
                    continue;
                }
                //Insert the user into MongoDB
                memberManager.getPrimaryComponent().generateUserFromConfig(UUID.fromString(key), Integer.parseInt(ontime));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
