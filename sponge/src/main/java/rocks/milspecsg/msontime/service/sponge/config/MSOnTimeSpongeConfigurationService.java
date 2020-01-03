package rocks.milspecsg.msontime.service.sponge.config;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.config.DefaultConfig;
import rocks.milspecsg.msontime.service.common.config.MSOnTimeConfigurationService;

public class MSOnTimeSpongeConfigurationService extends MSOnTimeConfigurationService {

    @Inject
    public MSOnTimeSpongeConfigurationService(@DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
    }
}
