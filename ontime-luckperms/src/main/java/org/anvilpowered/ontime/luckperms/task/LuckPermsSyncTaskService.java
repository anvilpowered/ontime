/*
 *   OnTime - AnvilPowered
 *   Copyright (C) 2020
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.ontime.luckperms.task;

import com.google.inject.Inject;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.ontime.api.member.MemberManager;
import org.anvilpowered.ontime.api.registry.OnTimeKeys;
import org.anvilpowered.ontime.common.task.CommonSyncTaskService;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public abstract class LuckPermsSyncTaskService<TUser, TPlayer, TString>
    extends CommonSyncTaskService {

    private final LuckPerms luckPerms;

    @Inject
    private MemberManager<TString> memberManager;

    @Inject
    private UserService<TUser, TPlayer> userService;

    protected LuckPermsSyncTaskService(Registry registry) {
        super(registry);
        luckPerms = LuckPermsProvider.get();
    }

    @Override
    public Runnable getSyncTask() {
        return () -> {
            Set<String> configRanks = registry.getOrDefault(OnTimeKeys.RANKS).keySet();
            for (TPlayer player : userService.getOnlinePlayers()) {
                UUID userUUID = userService.getUUID((TUser) player);
                User user = luckPerms.getUserManager().getUser(userUUID);
                if (user == null) {
                    continue;
                }
                Optional<String> multiplier = user.getNodes().stream()
                    .filter(NodeType.META::matches)
                    .map(NodeType.META::cast)
                    .filter(node -> MULTIPLIER_META_KEY.equals(node.getMetaKey()))
                    .findAny()
                    .map(MetaNode::getMetaValue);
                long time = 60;
                if (multiplier.isPresent()) {
                    try {
                        time *= Double.parseDouble(multiplier.get());
                    } catch (NumberFormatException e) {
                        logger.error("An error occurred parsing the time multiplier for "
                            + userService.getUserName((TUser) player), e);
                    }
                }
                memberManager.sync(userUUID, time).thenAcceptAsync(optionalRank -> {
                    if (!optionalRank.isPresent()) {
                        return;
                    }
                    String rank = optionalRank.get();
                    user.data().add(Node.builder("group." + rank).build());
                    for (String configRank : configRanks) {
                        if (!rank.equals(configRank)) {
                            user.data().remove(Node.builder("group." + configRank).build());
                        }
                    }
                    luckPerms.getUserManager().saveUser(user);
                });
            }
        };
    }
}
