package rocks.milspecsg.msontime.api.config;

import com.google.common.reflect.TypeToken;

import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public interface ConfigTypes {

    TypeToken<Map<String, Integer>> RANKS = new TypeToken<Map<String, Integer>>() {
    };;

    TypeToken<String> MONGODB_HOSTNAME = TypeToken.of(String.class);
    TypeToken<Integer> MONGODB_PORT = TypeToken.of(Integer.class);
    TypeToken<String> MONGODB_DBNAME = TypeToken.of(String.class);
    TypeToken<String> MONGODB_USERNAME = TypeToken.of(String.class);
    TypeToken<String> MONGODB_PASSWORD = TypeToken.of(String.class);
    TypeToken<Boolean> MONGODB_USE_AUTH = TypeToken.of(Boolean.class);
}
