/*
 *   OnTime - AnvilPowered
 *   Copyright (C) 2020 Cableguy20
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

package org.anvilpowered.ontime.common.member;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.util.StringResult;
import org.anvilpowered.anvil.api.util.TimeFormatService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.anvil.base.manager.BaseManager;
import org.anvilpowered.ontime.api.data.key.MSOnTimeKeys;
import org.anvilpowered.ontime.api.member.MemberManager;
import org.anvilpowered.ontime.api.member.repository.MemberRepository;
import org.anvilpowered.ontime.api.model.member.Member;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonMemberManager<
    TUser,
    TPlayer extends TCommandSource,
    TString,
    TCommandSource>
    extends BaseManager<MemberRepository<?, ?>>
    implements MemberManager<TString> {

    @Inject
    protected PluginInfo<TString> pluginInfo;

    @Inject
    protected StringResult<TString, TCommandSource> stringResult;

    @Inject
    protected TimeFormatService timeFormatService;

    @Inject
    protected UserService<TUser, TPlayer> userService;

    @Inject
    public CommonMemberManager(Registry registry) {
        super(registry);
    }

    @Override
    public CompletableFuture<TString> info(UUID userUUID) {
        String name = userService.getUserName(userUUID).orElse(userUUID.toString());
        return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (optionalMember.isPresent()) {
                Member<?> member = optionalMember.get();
                String totalTime = timeFormatService.format(
                    Duration.ofSeconds(member.getPlayTime() + member.getBonusTime())
                );
                return stringResult.builder()
                    .append(
                        stringResult.builder()
                            .dark_gray().append("========= ")
                            .gold().append(name)
                            .dark_gray().append(" =========")
                    )
                    .gray().append("\n\nTotal Time: ").aqua().append(totalTime)
                    .append("\n", getNextGroup(member))
                    .append("\n\n", stringResult.builder()
                        .dark_gray().append("========= ")
                        .gold().append(pluginInfo.getPrefix())
                        .dark_gray().append(" ========="))
                    .build();
            }
            return stringResult.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Could not find user ", name)
                .build();
        });
    }

    @Override
    public CompletableFuture<TString> infoExtended(UUID userUUID) {
        String name = userService.getUserName(userUUID).orElse(userUUID.toString());
        return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (optionalMember.isPresent()) {
                Member<?> member = optionalMember.get();
                String playTime = timeFormatService.format(
                    Duration.ofSeconds(member.getPlayTime())
                );
                String bonusTime = timeFormatService.format(
                    Duration.ofSeconds(member.getBonusTime())
                );
                String totalTime = timeFormatService.format(
                    Duration.ofSeconds(member.getPlayTime() + member.getBonusTime())
                );
                return stringResult.builder()
                    .append(stringResult.builder()
                        .dark_gray().append("========= ")
                        .gold().append(name)
                        .dark_gray().append(" =========")
                    )
                    .gray().append("\n\nPlay Time: ").aqua().append(playTime)
                    .gray().append("\nBonus Time: ").aqua().append(bonusTime)
                    .gray().append("\nTotal Time: ").aqua().append(totalTime)
                    .append("\n", getNextGroup(member))
                    .append("\n\n", stringResult.builder()
                        .dark_gray().append("========= ")
                        .gold().append(pluginInfo.getPrefix())
                        .dark_gray().append(" ========="))
                    .build();
            }
            return stringResult.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Could not find ", name)
                .build();
        });
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

                long playTime = optionalMember.get().getPlayTime();
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
    public CompletableFuture<TString> addBonusTime(UUID userUUID, long time) {
        String name = userService.getUserName(userUUID).orElse(userUUID.toString());
        return getPrimaryComponent().addBonusTimeForUser(userUUID, time).thenApplyAsync(result -> {
            if (result) {
                return stringResult.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("Successfully added ")
                    .gold().append(timeFormatService.format(Duration.ofSeconds(time)))
                    .green().append(" to ", name, "'s bonus time")
                    .build();
            }
            return stringResult.fail("Could not find " + name);
        });
    }

    @Override
    public CompletableFuture<TString> setBonusTime(UUID userUUID, long time) {
        String name = userService.getUserName(userUUID).orElse(userUUID.toString());
        return getPrimaryComponent().setBonusTimeForUser(userUUID, time).thenApplyAsync(result -> {
            if (result) {
                return stringResult.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("Successfully updated ", name, "'s bonus time to ")
                    .gold().append(timeFormatService.format(Duration.ofSeconds(time)))
                    .build();
            }
            return stringResult.fail("Could not find " + name);
        });
    }

    @Override
    public CompletableFuture<TString> setTotalTime(UUID userUUID, long time) {
        String name = userService.getUserName(userUUID).orElse(userUUID.toString());
        return getPrimaryComponent().setTotalTimeForUser(userUUID, time).thenApplyAsync(result -> {
            if (result) {
                return stringResult.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("Successfully updated ", name, "'s total time to ")
                    .gold().append(timeFormatService.format(Duration.ofSeconds(time)))
                    .build();
            }
            return stringResult.fail("Could not find " + name);
        });
    }

    protected TString getNextGroup(Member<?> member) {
        Map<String, Integer> ranks = registry.getOrDefault(MSOnTimeKeys.RANKS);
        long totalTime = member.getBonusTime() + member.getPlayTime();
        String[] nextRankPrefix = new String[]{"N/A"};
        long[] nextRankSeconds = new long[]{-1};
        ranks.forEach((k, v) -> {
            if (v > totalTime && (nextRankSeconds[0] < 0 || v < nextRankSeconds[0])) {
                nextRankPrefix[0] = k;
                nextRankSeconds[0] = v;
            }
        });
        return stringResult.builder()
            .gray().append("Next Rank: ")
            .aqua().append(nextRankPrefix[0] + " (" + nextRankSeconds[0] + " minutes) ")
            .gray().append("\nTime Remaining: ")
            .aqua().append(nextRankSeconds[0] > 0 ? nextRankSeconds[0] - totalTime : -1)
            .build();
    }
}
