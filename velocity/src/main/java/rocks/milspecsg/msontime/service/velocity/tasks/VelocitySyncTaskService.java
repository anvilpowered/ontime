package rocks.milspecsg.msontime.service.velocity.tasks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.Scheduler;
import net.kyori.text.TextComponent;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.track.Track;
import org.slf4j.Logger;
import rocks.milspecsg.msontime.api.member.MemberManager;
import rocks.milspecsg.msontime.plugin.MSOnTime;
import rocks.milspecsg.msontime.service.common.tasks.CommonSyncTaskService;
import rocks.milspecsg.msrepository.api.data.registry.Registry;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Singleton
public class VelocitySyncTaskService extends CommonSyncTaskService {

    private Scheduler task;

    @Inject
    LuckPerms luckPerms;

    @Inject
    Logger logger;

    @Inject
    ProxyServer proxyServer;

    @Inject
    MemberManager<TextComponent> memberManager;

    @Inject
    public VelocitySyncTaskService(Registry registry) {
        super(registry);
    }

    @Override
    public void startSyncTask() {
        logger.info("Starting sync task.");
        proxyServer.getEventManager().register(this, task.buildTask(MSOnTime.plugin, getSyncTask()).repeat(1, TimeUnit.MINUTES).schedule());
    }

    @Override
    public void stopSyncTask() {
        //TODO Implement task stopping
    }

    @Override
    public Runnable getSyncTask() {
        return () -> {
            Track track = luckPerms.getTrackManager().getTrack("groups");
            if (!track.getGroups().isEmpty()) {
                List<String> groups = track.getGroups();

                proxyServer.getAllPlayers().forEach(player ->
                    memberManager.sync(player.getUniqueId()).thenAcceptAsync(optionalRank -> {
                        if (!optionalRank.isPresent()) {
                            return;
                        }
                        String rank = optionalRank.get();
                        for (String group : groups) {
                            if (group.equals(rank)) {
                                luckPerms.getUserManager().getUser(player.getUniqueId()).setPrimaryGroup(rank);
                            }
                        }
                    }));
            } else {
                throw new NullPointerException("You must specify a track in the conifg, and the track must contain groups!");
            }
        };
    }
}
