package rocks.milspecsg.msontime.service.common.member;

import com.google.inject.Inject;
import rocks.milspecsg.msontime.api.config.ConfigKeys;
import rocks.milspecsg.msontime.api.config.ConfigTypes;
import rocks.milspecsg.msontime.api.member.MemberManager;
import rocks.milspecsg.msontime.api.member.repository.MemberRepository;
import rocks.milspecsg.msrepository.PluginInfo;
import rocks.milspecsg.msrepository.api.UserService;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;
import rocks.milspecsg.msrepository.api.tools.resultbuilder.StringResult;
import rocks.milspecsg.msrepository.service.common.manager.CommonManager;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonMemberManager<
        TUser,
        TString,
        TCommandSource>
        extends CommonManager<MemberRepository<?, ?, ?>>
        implements MemberManager<TString> {

    @Inject
    PluginInfo<TString> pluginInfo;

    @Inject
    StringResult<TString, TCommandSource> stringResult;

    @Inject
    UserService<TUser> userService;

    @Inject
    public CommonMemberManager(ConfigurationService configurationService) {
        super(configurationService);
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
                        .append("\n")
                        .gray().append("\nPlay Time: ").aqua().append(optionalMember.get().getPlayTime())
                        .gray().append("\nBonus Time: ").aqua().append(optionalMember.get().getBonusTime())
                        .gray().append("\nEffective Time: ").aqua().append(optionalMember.get().getPlayTime() + optionalMember.get().getBonusTime())
                        .append("\n").append(getNextGroup(optionalMember.get().getUserUUID()).join())
                        .append("\n")
                        .append(stringResult.builder().dark_gray().append("\n========= ").gold().append(pluginInfo.getPrefix()).dark_gray().append(" ========="))
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
                configurationService.getConfigMap(ConfigKeys.RANKS, ConfigTypes.RANKS).forEach((k, v) -> {
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
    public CompletableFuture<TString> setBonusTime(UUID userUUID, int bonusTime) {
        return getPrimaryComponent().setBonusTimeForUser(userUUID, bonusTime).thenApplyAsync(result -> {
            if (!result) {
                return stringResult.fail("Invalid user");
            }

            return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
                if (!optionalMember.isPresent()) {
                    return stringResult.fail("Invalid user");
                }

                int effectiveTime = optionalMember.get().getPlayTime() + optionalMember.get().getBonusTime();
                return stringResult.success("Set bonus play time to " + effectiveTime);
            }).join();
        });
    }

    @Override
    public CompletableFuture<TString> addBonusTime(UUID userUUID, int bonusTime) {
        return getPrimaryComponent().addBonusTimeForUser(userUUID, bonusTime).thenApplyAsync(result -> {
            if (!result) {
                return stringResult.fail("Invalid user");
            }
            return stringResult.success("Added " + bonusTime + " to " + userService.getUserName(userUUID));
        });
    }

    @Override
    public CompletableFuture<TString> getNextGroup(UUID userUUID) {
        return getPrimaryComponent().getOneForUser(userUUID).thenApplyAsync(optionalUser -> {
            if (optionalUser.isPresent()) {
                Map<String, Integer> ranks = configurationService.getConfigMap(ConfigKeys.RANKS, ConfigTypes.RANKS);
                int effectiveTime = optionalUser.get().getBonusTime() + optionalUser.get().getPlayTime();
                String[] nextRankPrefix = new String[]{"N/A"};
                int[] nextRankMinutes = new int[]{-1};
                ranks.forEach((k, v) -> {
                    if (v > effectiveTime && nextRankMinutes[0] < 0) {
                        nextRankPrefix[0] = k;
                        nextRankMinutes[0] = v;
                    }
                });


                return stringResult.builder().gray().append("Next Rank: ").aqua().append(nextRankPrefix[0] + " (" + nextRankMinutes[0] + " minutes) ")
                        .gray().append("\nTime Remaining: ").aqua().append(nextRankMinutes[0] - effectiveTime).build();
            } else {
                return stringResult.fail("Couldn't find data for " + userService.getUserName(userUUID));
            }
        });
    }
}
