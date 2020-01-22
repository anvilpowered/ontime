package rocks.milspecsg.msontime.plugin;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.Plugin;
import rocks.milspecsg.msontime.api.tasks.SyncTaskService;
import rocks.milspecsg.msontime.module.VelocityModule;
import rocks.milspecsg.msrepository.api.data.registry.Registry;
import rocks.milspecsg.msrepository.api.util.PluginInfo;
import rocks.milspecsg.msrepository.velocity.module.ApiVelocityModule;

@Plugin(id = MSOnTimePluginInfo.id,
    name = MSOnTimePluginInfo.name,
    version = MSOnTimePluginInfo.version,
    description = MSOnTimePluginInfo.description,
    url = MSOnTimePluginInfo.url,
    authors = MSOnTimePluginInfo.authors
)
public class MSOnTime {

    @Inject
    Injector velocityRootInjector;

    @Inject
    private Logger logger;

    @Override
    public String toString() {
        return MSOnTimePluginInfo.id;
    }

    public static MSOnTime plugin = null;
    private Injector injector = null;


    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        plugin = this;
        injector = velocityRootInjector.createChildInjector(new VelocityModule(), new ApiVelocityModule());
        logger.info("Loading...");
        injector.getInstance(SyncTaskService.class);
        loadRegistry();
    }

    private void loadRegistry() {
        injector.getInstance(Registry.class).load(this);
    }

}
