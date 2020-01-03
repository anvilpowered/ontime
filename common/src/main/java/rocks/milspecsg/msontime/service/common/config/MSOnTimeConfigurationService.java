package rocks.milspecsg.msontime.service.common.config;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msontime.api.ConfigKeys;
import rocks.milspecsg.msrepository.service.common.config.CommonConfigurationService;

public class MSOnTimeConfigurationService extends CommonConfigurationService {

    private static TypeToken<String> stringTypeToken = new TypeToken<String>() {
    };
    private static TypeToken<Integer> integerTypeToken = new TypeToken<Integer>() {
    };
    private static TypeToken<Boolean> booleanTypeToken = new TypeToken<Boolean>() {
    };

    @Inject
    public MSOnTimeConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
    }

    @Override
    protected void initNodeTypeMap() {
        nodeTypeMap.put(ConfigKeys.MONGODB_HOSTNAME, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_PORT, integerTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_DBNAME, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_USERNAME, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_PASSWORD, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_USE_AUTH, booleanTypeToken);
        nodeTypeMap.put(ConfigKeys.DATA_STORE_NAME, stringTypeToken);
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
    }
}
