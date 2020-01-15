package rocks.milspecsg.msontime;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msontime.api.tasks.SyncTaskService;
import rocks.milspecsg.msontime.commands.OnTimeCommandManager;
import rocks.milspecsg.msontime.listeners.PlayerListener;
import rocks.milspecsg.msrepository.api.data.registry.Registry;
import rocks.milspecsg.msrepository.api.util.PluginInfo;
import rocks.milspecsg.msrepository.sponge.module.ApiSpongeModule;

@Plugin(
    id = MSOnTimePluginInfo.id,
    name = MSOnTimePluginInfo.name,
    version = MSOnTimePluginInfo.version,
    description = MSOnTimePluginInfo.description,
    authors = MSOnTimePluginInfo.authors,
    url = MSOnTimePluginInfo.url,
    dependencies = @Dependency(id = "mscore")
)
public class MSOnTime {

    @Override
    public String toString() {
        return MSOnTimePluginInfo.id;
    }

    @Inject
    private Injector spongeRootInjector;

    @Inject
    private Logger logger;

    public static MSOnTime plugin = null;
    private Injector injector = null;
    private PluginInfo<Text> pluginInfo = null;

    private boolean alreadyLoadedOnce = false;

    @Listener
    public void onServerInitialization(GameInitializationEvent event) {
        plugin = this;
        injector = spongeRootInjector.createChildInjector(new SpongeModule(), new ApiSpongeModule());
        pluginInfo = injector.getInstance(Key.get(new TypeLiteral<PluginInfo<Text>>() {
        }));
        Sponge.getServer().getConsole().sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.AQUA, "Loading..."));
        initCommands();
        injector.getInstance(SyncTaskService.class);
        Sponge.getEventManager().registerListeners(this, injector.getInstance(PlayerListener.class));
        loadRegistry();
        Sponge.getServer().getConsole().sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.AQUA, "Done"));
    }

    @Listener
    public void reload(GameReloadEvent event) {
        loadRegistry();
        logger.info("Reloaded successfully!");
    }

    @Listener
    public void stop(GameStoppingEvent event) {
        Sponge.getServer().getConsole().sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.AQUA, "Stopping"));
    }

    private void loadRegistry() {
        injector.getInstance(Registry.class).load(this);
    }

    private void initCommands() {
        if (!alreadyLoadedOnce) {
            injector.getInstance(OnTimeCommandManager.class).register(this);
            alreadyLoadedOnce = true;
        }
    }
}
