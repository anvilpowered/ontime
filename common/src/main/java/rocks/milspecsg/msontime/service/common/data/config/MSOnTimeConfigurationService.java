package rocks.milspecsg.msontime.service.common.data.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msontime.api.data.key.MSOnTimeKeys;
import rocks.milspecsg.msrepository.api.data.key.Keys;
import rocks.milspecsg.msrepository.common.data.config.CommonConfigurationService;

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
