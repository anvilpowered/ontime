package rocks.milspecsg.msontime.service.sponge.tasks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msontime.MSOnTime;
import rocks.milspecsg.msontime.api.member.MemberManager;
import rocks.milspecsg.msontime.service.common.tasks.CommonSyncTaskService;
import rocks.milspecsg.msrepository.PluginInfo;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

import java.util.concurrent.TimeUnit;

@Singleton
public class SpongeSyncTaskService extends CommonSyncTaskService {

    @Inject
    MemberManager<Text> memberManager;

    @Inject
    PluginInfo<Text> pluginInfo;

    private Task task;

    @Inject
    public SpongeSyncTaskService(ConfigurationService configurationService) {
        super(configurationService);
    }

    @Override
    public void startSyncTask() {
        Sponge.getServer().getConsole().sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.AQUA, "Starting playtime sync task"));
        task = Task.builder().async().interval(1, TimeUnit.MINUTES).execute(getSyncTask()).submit(MSOnTime.plugin);
    }

    @Override
    public void stopSyncTask() {
        if (task != null) task.cancel();
    }

    @Override
    public Runnable getSyncTask() {
        return () -> Sponge.getServer().getOnlinePlayers().forEach(player ->
            memberManager.sync(player.getUniqueId()).thenAcceptAsync(optionalRank ->
                Sponge.getServer().getConsole().sendMessage(Text.of(TextColors.GREEN, player.getName(), " : ", optionalRank.orElse("null")))
            )
        );
    }
}
