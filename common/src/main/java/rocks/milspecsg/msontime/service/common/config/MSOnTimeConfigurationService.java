package rocks.milspecsg.msontime.service.common.config;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msontime.api.config.ConfigKeys;
import rocks.milspecsg.msontime.api.config.ConfigTypes;
import rocks.milspecsg.msrepository.service.common.config.CommonConfigurationService;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class MSOnTimeConfigurationService extends CommonConfigurationService {

    @Inject
    public MSOnTimeConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
    }

    @Override
    protected void initNodeTypeMap() {
        nodeTypeMap.put(ConfigKeys.MONGODB_HOSTNAME, ConfigTypes.MONGODB_HOSTNAME);
        nodeTypeMap.put(ConfigKeys.MONGODB_PORT, ConfigTypes.MONGODB_PORT);
        nodeTypeMap.put(ConfigKeys.MONGODB_DBNAME, ConfigTypes.MONGODB_DBNAME);
        nodeTypeMap.put(ConfigKeys.MONGODB_USERNAME, ConfigTypes.MONGODB_USERNAME);
        nodeTypeMap.put(ConfigKeys.MONGODB_PASSWORD, ConfigTypes.MONGODB_PASSWORD);
        nodeTypeMap.put(ConfigKeys.MONGODB_USE_AUTH, ConfigTypes.MONGODB_USE_AUTH);
        nodeTypeMap.put(ConfigKeys.DATA_STORE_NAME, TypeToken.of(String.class));
        nodeTypeMap.put(ConfigKeys.RANKS, ConfigTypes.RANKS);
    }

    @Override
    protected void initVerificationMaps() {

    }

    @Override
    protected void initDefaultMaps() {
        defaultStringMap.put(ConfigKeys.MONGODB_HOSTNAME, "localhost");
        defaultIntegerMap.put(ConfigKeys.MONGODB_PORT, 27017);
        defaultStringMap.put(ConfigKeys.MONGODB_DBNAME, "msontime");
        defaultStringMap.put(ConfigKeys.MONGODB_USERNAME, "admin");
        defaultStringMap.put(ConfigKeys.MONGODB_PASSWORD, "password");
        defaultBooleanMap.put(ConfigKeys.MONGODB_USE_AUTH, false);
        defaultStringMap.put(ConfigKeys.DATA_STORE_NAME, "mongodb");

        Map<String, Integer> defaultRankMap = new HashMap<>();

        defaultRankMap.put("pv1", 3);
        defaultRankMap.put("pv2", 5);
        defaultRankMap.put("pfc", 7);

        defaultMapMap.put(ConfigKeys.RANKS, defaultRankMap);
    }

    @Override
    protected void initNodeNameMap() {
        nodeNameMap.put(ConfigKeys.MONGODB_HOSTNAME, "datastore.mongodb.hostname");
        nodeNameMap.put(ConfigKeys.MONGODB_PORT, "datastore.mongodb.port");
        nodeNameMap.put(ConfigKeys.MONGODB_DBNAME, "datastore.mongodb.dbName");
        nodeNameMap.put(ConfigKeys.MONGODB_USERNAME, "datastore.mongodb.username");
        nodeNameMap.put(ConfigKeys.MONGODB_PASSWORD, "datastore.mongodb.password");
        nodeNameMap.put(ConfigKeys.MONGODB_USE_AUTH, "datastore.mongodb.useAuth");
        nodeNameMap.put(ConfigKeys.DATA_STORE_NAME, "datastore.dataStoreName");
        nodeNameMap.put(ConfigKeys.RANKS, "ranks");
    }

    @Override
    protected void initNodeDescriptionMap() {
        nodeDescriptionMap.put(ConfigKeys.MONGODB_HOSTNAME, "\nMongoDB hostname");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_PORT, "\nMongoDB port");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_DBNAME, "\nMongoDB database name");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_USERNAME, "\nMongoDB username");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_PASSWORD, "\nMongoDB password");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_USE_AUTH, "\nWhether to use authentication (username/password) for MongoDB connection");
        nodeDescriptionMap.put(ConfigKeys.DATA_STORE_NAME, "\nData store name");
        nodeDescriptionMap.put(ConfigKeys.RANKS, "\nRank time definitions");
    }
}
