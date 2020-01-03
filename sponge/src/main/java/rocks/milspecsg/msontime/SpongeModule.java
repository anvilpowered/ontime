package rocks.milspecsg.msontime;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msontime.service.common.config.MSOnTimeConfigurationService;
import rocks.milspecsg.msontime.service.sponge.config.MSOnTimeSpongeConfigurationService;

public class SpongeModule extends CommonModule<User, Text, CommandSource> {

    @Override
    protected void configure() {
        super.configure();

        bind(MSOnTimeConfigurationService.class).to(MSOnTimeSpongeConfigurationService.class);
    }
}
