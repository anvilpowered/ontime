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

package rocks.milspecsg.msontime.common.member;

import com.google.inject.Inject;
import rocks.milspecsg.msontime.api.data.key.MSOnTimeKeys;
import rocks.milspecsg.msontime.api.member.MemberManager;
import rocks.milspecsg.msontime.api.member.repository.MemberRepository;
import rocks.milspecsg.msrepository.api.data.registry.Registry;
import rocks.milspecsg.msrepository.api.util.PluginInfo;
import rocks.milspecsg.msrepository.api.util.StringResult;
import rocks.milspecsg.msrepository.api.util.UserService;
import rocks.milspecsg.msrepository.common.manager.CommonManager;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonMemberManager<
    TUser,
    TPlayer extends TCommandSource,
    TString,
    TCommandSource>
    extends CommonManager<MemberRepository<?, ?>>
    implements MemberManager<TString> {

    @Inject
    PluginInfo<TString> pluginInfo;

    @Inject
    StringResult<TString, TCommandSource> stringResult;

    @Inject
    UserService<TUser, TPlayer> userService;

    @Inject
    public CommonMemberManager(Registry registry) {
        super(registry);
    }

    @Override
    public CompletableFuture<TString> info(UUID userUUID) {
        return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember ->
            optionalMember.isPresent()
                ? stringResult.builder()
                .append(stringResult.builder().dark_gray().append("========= ").gold().append(userService.getUserName(userUUID).orElse("null")).dark_gray().append(" ========="))
                .append("\n")
                .gray().append("\nPlayTime: ").aqua().append(optionalMember.get().getPlayTime() + optionalMember.get().getBonusTime())
                .append("\n")
                .append(getNextGroup(optionalMember.get().getUserUUID()).join())
                .append("\n")
                .append(stringResult.builder().dark_gray().append("\n========= ").gold().append(pluginInfo.getPrefix()).dark_gray().append(" ========="))
                .build()
                : stringResult.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Could not find user ", userService.getUserName(userUUID).orElse(userUUID.toString()))
                .build());
    }

    @Override
    public CompletableFuture<TString> infoExtended(UUID userUUID) {
        return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember ->
            optionalMember.isPresent()
                ? stringResult.builder()
                .append(stringResult.builder().dark_gray().append("========= ").gold().append(userService.getUserName(userUUID).orElse("null")).dark_gray().append(" ========="))
                .gray().append("\n\nPlay Time: ").aqua().append(optionalMember.get().getPlayTime())
                .gray().append("\nBonus Time: ").aqua().append(optionalMember.get().getBonusTime())
                .gray().append("\nEffective Time: ").aqua().append(optionalMember.get().getPlayTime() + optionalMember.get().getBonusTime())
                .append("\n", getNextGroup(optionalMember.get().getUserUUID()).join())
                .append(stringResult.builder().dark_gray().append("\n\n========= ").gold().append(pluginInfo.getPrefix()).dark_gray().append(" ========="))
                .build()
                : stringResult.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Could not find user ", userService.getUserName(userUUID).orElse(userUUID.toString()))
                .build());
    }

    @Override
    public CompletableFuture<Optional<String>> sync(UUID userUUID) {
        return getPrimaryComponent().addMinuteForUser(userUUID).thenApplyAsync(result -> {
            if (!result) {
                return Optional.empty();
            }

            return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
                if (!optionalMember.isPresent()) {
                    return Optional.<String>empty();
                }

                int playTime = optionalMember.get().getPlayTime();
                String[] highestRank = {null};
                int[] highestPlayTime = {0};
                registry.getOrDefault(MSOnTimeKeys.RANKS).forEach((k, v) -> {
                    if (v > highestPlayTime[0] && v <= playTime) {
                        highestPlayTime[0] = v;
                        highestRank[0] = k;
                    }
                });
                return Optional.ofNullable(highestRank[0]);
            }).join();
        });
    }

    @Override
    public CompletableFuture<TString> addBonusTime(UUID userUUID, int time) {
        return getPrimaryComponent().addBonusTimeForUser(userUUID, time).thenApplyAsync(result -> {
            if (result) {
                return stringResult.success("Added " + time + " to " + userService.getUserName(userUUID).orElse("null"));
            }
            return stringResult.fail("Invalid user");
        });
    }

    @Override
    public CompletableFuture<TString> setBonusTime(UUID userUUID, int time) {
        return getPrimaryComponent().setBonusTimeForUser(userUUID, time).thenApplyAsync(result -> {
            if (result) {
                return stringResult.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("Successfully updated ", userService.getUserName(userUUID).orElse("null"), "'s bonus time to ")
                    .gold().append(time)
                    .build();
            }
            return stringResult.fail("Invalid user");
        });
    }

    @Override
    public CompletableFuture<TString> setTotalTime(UUID userUUID, int time) {
        return getPrimaryComponent().setTotalTimeForUser(userUUID, time).thenApplyAsync(result -> {
            if (result) {
                return stringResult.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("Successfully updated ", userService.getUserName(userUUID).orElse("null"), "'s total time to ")
                    .gold().append(time)
                    .build();
            }
            return stringResult.fail("Invalid user");
        });
    }

    @Override
    public CompletableFuture<TString> getNextGroup(UUID userUUID) {
        return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalUser -> {
            if (optionalUser.isPresent()) {
                Map<String, Integer> ranks = registry.getOrDefault(MSOnTimeKeys.RANKS);
                int effectiveTime = optionalUser.get().getBonusTime() + optionalUser.get().getPlayTime();
                String[] nextRankPrefix = new String[]{"N/A"};
                int[] nextRankMinutes = new int[]{-1};
                ranks.forEach((k, v) -> {
                    if (v > effectiveTime && (nextRankMinutes[0] < 0 || v < nextRankMinutes[0])) {
                        nextRankPrefix[0] = k;
                        nextRankMinutes[0] = v;
                    }
                });
                return stringResult.builder().gray().append("Next Rank: ").aqua().append(nextRankPrefix[0] + " (" + nextRankMinutes[0] + " minutes) ")
                    .gray().append("\nTime Remaining: ").aqua().append(nextRankMinutes[0] > 0 ? nextRankMinutes[0] - effectiveTime : -1).build();
            } else {
                return stringResult.fail("Couldn't find data for " + userService.getUserName(userUUID));
            }
        });
    }
}
