package rocks.milspecsg.msontime.sponge.data.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.config.DefaultConfig;
import rocks.milspecsg.msontime.common.data.config.MSOnTimeConfigurationService;

@Singleton
public class MSOnTimeSpongeConfigurationService extends MSOnTimeConfigurationService {

    @Inject
    public MSOnTimeSpongeConfigurationService(@DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
    }
}
