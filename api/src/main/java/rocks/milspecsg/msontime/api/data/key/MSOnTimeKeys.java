package rocks.milspecsg.msontime.api.data.key;

import rocks.milspecsg.msrepository.api.data.key.Key;
import rocks.milspecsg.msrepository.api.data.key.Keys;

import java.util.HashMap;
import java.util.Map;

public final class MSOnTimeKeys {

    private MSOnTimeKeys() {
        throw new AssertionError("**boss music** No instance for you!");
    }

    public static final Key<Map<String, Integer>> RANKS = new Key<Map<String, Integer>>("RANKS", new HashMap<>()) {
    };

    static {
        Keys.registerKey(RANKS);
    }
}
