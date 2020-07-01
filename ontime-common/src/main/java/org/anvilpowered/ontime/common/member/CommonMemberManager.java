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

package org.anvilpowered.ontime.common.member;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.TimeFormatService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.anvil.base.datastore.BaseManager;
import org.anvilpowered.ontime.api.data.key.OnTimeKeys;
import org.anvilpowered.ontime.api.member.MemberManager;
import org.anvilpowered.ontime.api.member.repository.MemberRepository;
import org.anvilpowered.ontime.api.model.member.Member;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class CommonMemberManager<
    TUser,
    TPlayer,
    TString,
    TCommandSource>
    extends BaseManager<MemberRepository<?, ?>>
    implements MemberManager<TString> {

    @Inject
    protected PluginInfo<TString> pluginInfo;

    @Inject
    protected TextService<TString, TCommandSource> textService;

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
        return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            String name = userService.getUserName(userUUID).join().orElse(userUUID.toString());
            if (optionalMember.isPresent()) {
                Member<?> member = optionalMember.get();
                String totalTime = timeFormatService.format(
                    Duration.ofSeconds(member.getPlayTime() + member.getBonusTime())
                ).toString();
                return textService.builder()
                    .append(
                        textService.builder()
                            .dark_gray().append("========= ")
                            .gold().append(name)
                            .dark_gray().append(" =========")
                    )
                    .gray().append("\n\nTotal Time: ").aqua().append(totalTime)
                    .append(getGroupInfo(member))
                    .append("\n\n", textService.builder()
                        .dark_gray().append("========= ")
                        .gold().append(pluginInfo.getPrefix())
                        .dark_gray().append("========="))
                    .build();
            }
            return getNotFoundError(name);
        });
    }

    @Override
    public CompletableFuture<TString> infoExtended(UUID userUUID) {
        return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            String name = userService.getUserName(userUUID).join().orElse(userUUID.toString());
            if (optionalMember.isPresent()) {
                Member<?> member = optionalMember.get();
                String playTime = timeFormatService.format(
                    Duration.ofSeconds(member.getPlayTime())
                ).toString();
                String bonusTime = timeFormatService.format(
                    Duration.ofSeconds(member.getBonusTime())
                ).toString();
                String totalTime = timeFormatService.format(
                    Duration.ofSeconds(member.getPlayTime() + member.getBonusTime())
                ).toString();
                return textService.builder()
                    .append(textService.builder()
                        .dark_gray().append("========= ")
                        .gold().append(name)
                        .dark_gray().append(" =========")
                    )
                    .gray().append("\n\nPlay Time: ").aqua().append(playTime)
                    .gray().append("\nBonus Time: ").aqua().append(bonusTime)
                    .gray().append("\nTotal Time: ").aqua().append(totalTime)
                    .append(getGroupInfo(member))
                    .append("\n\n", textService.builder()
                        .dark_gray().append("========= ")
                        .gold().append(pluginInfo.getPrefix())
                        .dark_gray().append(" ========="))
                    .build();
            }
            return getNotFoundError(name);
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

                long totalTime = optionalMember.get().getPlayTime()
                    + optionalMember.get().getBonusTime();
                String[] highestRank = {null};
                long[] highestPlayTime = {Long.MIN_VALUE};
                registry.getOrDefault(OnTimeKeys.RANKS).forEach((k, v) -> {
                    if (v >= highestPlayTime[0] && v <= totalTime) {
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
        return getPrimaryComponent().addBonusTimeForUser(userUUID, time).thenApplyAsync(result -> {
            String name = userService.getUserName(userUUID).join().orElse(userUUID.toString());
            if (result) {
                return textService.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("Successfully added ")
                    .gold().append(timeFormatService.format(Duration.ofSeconds(time)))
                    .green().append(" to ", name, "'s bonus time")
                    .build();
            }
            return getNotFoundError(name);
        });
    }

    private CompletableFuture<TString> convertFromString(
        BiFunction<UUID, Long, CompletableFuture<TString>> function,
        UUID userUUID, String time
    ) {
        Optional<Long> optionalTime = timeFormatService.parseSeconds(time);
        if (optionalTime.isPresent()) {
            return function.apply(userUUID, optionalTime.get());
        }
        return CompletableFuture.completedFuture(
            textService.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Failed to parse ")
                .gold().append(time)
                .build()
        );
    }

    @Override
    public CompletableFuture<TString> addBonusTime(UUID userUUID, String time) {
        return convertFromString(this::addBonusTime, userUUID, time);
    }

    @Override
    public CompletableFuture<TString> setBonusTime(UUID userUUID, long time) {
        return getPrimaryComponent().setBonusTimeForUser(userUUID, time).thenApplyAsync(result -> {
            String name = userService.getUserName(userUUID).join().orElse(userUUID.toString());
            if (result) {
                return textService.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("Successfully updated ", name, "'s bonus time to ")
                    .gold().append(timeFormatService.format(Duration.ofSeconds(time)))
                    .build();
            }
            return getNotFoundError(name);
        });
    }

    @Override
    public CompletableFuture<TString> setBonusTime(UUID userUUID, String time) {
        return convertFromString(this::setBonusTime, userUUID, time);
    }

    @Override
    public CompletableFuture<TString> setTotalTime(UUID userUUID, long time) {
        return getPrimaryComponent().setTotalTimeForUser(userUUID, time).thenApplyAsync(result -> {
            String name = userService.getUserName(userUUID).join().orElse(userUUID.toString());
            if (result) {
                return textService.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("Successfully updated ", name, "'s total time to ")
                    .gold().append(timeFormatService.format(Duration.ofSeconds(time)))
                    .build();
            }
            return getNotFoundError(name);
        });
    }

    @Override
    public CompletableFuture<TString> setTotalTime(UUID userUUID, String time) {
        return convertFromString(this::setTotalTime, userUUID, time);
    }

    protected TString getGroupInfo(Member<?> member) {
        Map<String, Integer> ranks = registry.getOrDefault(OnTimeKeys.RANKS);
        long totalTime = member.getBonusTime() + member.getPlayTime();
        // current rank, next rank
        String[] rankPrefix = new String[]{"N/A", "N/A"};
        long[] rankSeconds = new long[]{Long.MIN_VALUE, Long.MIN_VALUE};
        ranks.forEach((k, v) -> {
            if (v >= rankSeconds[0] && v <= totalTime) {
                rankSeconds[0] = v;
                rankPrefix[0] = k;
            }
            if (v > totalTime && (rankSeconds[1] < 0 || v < rankSeconds[1])) {
                rankPrefix[1] = k;
                rankSeconds[1] = v;
            }
        });
        return textService.builder()
            .gray().append("\nCurrent Rank: ")
            .aqua().append(rankPrefix[0], " (")
            .append(rankSeconds[0] >= 0
                ? timeFormatService.format(Duration.ofSeconds(rankSeconds[0]))
                : -1)
            .append(")")
            .gray().append("\nNext Rank: ")
            .aqua().append(rankPrefix[1], " (")
            .append(rankSeconds[1] >= 0
                ? timeFormatService.format(Duration.ofSeconds(rankSeconds[1]))
                : -1)
            .append(")")
            .gray().append("\nTime Remaining: ")
            .aqua().append(rankSeconds[1] > 0
                ? timeFormatService.format(Duration.ofSeconds(rankSeconds[1] - totalTime))
                : -1
            )
            .build();
    }

    protected TString getNotFoundError(String name) {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("Could not find ", name)
            .build();
    }
}
