package rocks.milspecsg.msontime.sponge.module;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msontime.common.module.CommonModule;
import rocks.milspecsg.msontime.api.tasks.SyncTaskService;
import rocks.milspecsg.msontime.common.data.config.MSOnTimeConfigurationService;
import rocks.milspecsg.msontime.sponge.data.config.MSOnTimeSpongeConfigurationService;
import rocks.milspecsg.msontime.sponge.tasks.SpongeSyncTaskService;

public class SpongeModule extends CommonModule<User, Player, Text, CommandSource> {

    @Override
    protected void configure() {
        super.configure();

        bind(MSOnTimeConfigurationService.class).to(MSOnTimeSpongeConfigurationService.class);

        bind(SyncTaskService.class).to(SpongeSyncTaskService.class);
    }
}
