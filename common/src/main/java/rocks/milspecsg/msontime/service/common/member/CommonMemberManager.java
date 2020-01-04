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
                .append(pluginInfo.getPrefix())
                .blue().append("\nUsername: ").aqua().append(userService.getUserName(userUUID).orElse("null"))
                .blue().append("\nPlayTime: ").aqua().append(optionalMember.get().getPlayTime())
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
}
