/*
 *     MSOnTime - MilSpecSG
 *     Copyright (C) 2019 Cableguy20
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package rocks.milspecsg.msontime.sponge.tasks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msontime.api.data.key.MSOnTimeKeys;
import rocks.milspecsg.msontime.api.member.MemberManager;
import rocks.milspecsg.msontime.common.tasks.CommonSyncTaskService;
import rocks.milspecsg.msontime.sponge.plugin.MSOnTime;
import rocks.milspecsg.msrepository.api.data.registry.Registry;
import rocks.milspecsg.msrepository.api.plugin.PluginInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Singleton
public class SpongeSyncTaskService extends CommonSyncTaskService {

    @Inject
    MemberManager<Text> memberManager;

    @Inject
    PluginInfo<Text> pluginInfo;

    PermissionService permissionService;

    private Task task;

    @Inject
    public SpongeSyncTaskService(Registry registry) {
        super(registry);
        permissionService = Sponge.getServiceManager().provide(PermissionService.class).orElseThrow(() -> new IllegalStateException("Missing PermissionService"));
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
        return () -> {
            Collection<Subject> allGroups = permissionService.getGroupSubjects().getLoadedSubjects();
            Set<String> configRanks = registry.getOrDefault(MSOnTimeKeys.RANKS).keySet();
            Sponge.getServer().getOnlinePlayers().forEach(player ->
                memberManager.sync(player.getUniqueId()).thenAcceptAsync(optionalRank -> {
                    if (!optionalRank.isPresent()) {
                        return;
                    }
                    String rank = optionalRank.get();
                    boolean hasNewRank = false;
                    for (Subject subject : allGroups) {
                        if (subject.getIdentifier().equals(rank)) {
                            player.getSubjectData().addParent(Collections.emptySet(), subject.asSubjectReference());
                            hasNewRank = true;
                        } else if (hasNewRank && player.getParents().size() == 1) {
                            break;
                        } else if (configRanks.contains(subject.getIdentifier())) {
                            player.getSubjectData().removeParent(Collections.emptySet(), subject.asSubjectReference());
                        }
                    }
                })
            );
        };
    }
}
