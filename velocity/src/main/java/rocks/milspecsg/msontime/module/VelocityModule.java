package rocks.milspecsg.msontime.module;

import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msontime.CommonModule;
import rocks.milspecsg.msontime.api.tasks.SyncTaskService;
import rocks.milspecsg.msontime.plugin.MSOnTimePluginInfo;
import rocks.milspecsg.msontime.service.velocity.tasks.VelocitySyncTaskService;
import rocks.milspecsg.msrepository.api.util.BasicPluginInfo;

import java.io.File;
import java.nio.file.Paths;


@Singleton
@SuppressWarnings({"unchecked", "UnstableApiUsage"})
public class VelocityModule extends CommonModule<
    TextComponent,
    Player,
    Player,
    CommandSource> {

    @Override
    protected void configure() {
        super.configure();

        File configFilesLocation = Paths.get("plugins/" + MSOnTimePluginInfo.id).toFile();

        if (!configFilesLocation.exists()) {
            if (!configFilesLocation.mkdirs()) {
                throw new IllegalStateException("Unable to create conifg. Please report this on github!");
            }
        }

        bind(new TypeLiteral<ConfigurationLoader<CommentedConfigurationNode>>() {
        }).toInstance(HoconConfigurationLoader.builder().setPath(Paths.get(configFilesLocation + "/msontime.conf")).build());

        bind(BasicPluginInfo.class).to(MSOnTimePluginInfo.class);

        bind(SyncTaskService.class).to(VelocitySyncTaskService.class);
    }
}
