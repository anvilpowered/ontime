package rocks.milspecsg.msontime.service.common.util;

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
