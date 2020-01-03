package rocks.milspecsg.msontime.service.common.member;

import com.google.inject.Inject;
import rocks.milspecsg.msontime.api.member.MemberManager;
import rocks.milspecsg.msontime.api.member.repository.MemberRepository;
import rocks.milspecsg.msrepository.PluginInfo;
import rocks.milspecsg.msrepository.api.UserService;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;
import rocks.milspecsg.msrepository.api.tools.resultbuilder.StringResult;
import rocks.milspecsg.msrepository.service.common.manager.CommonManager;

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
        return getPrimaryComponent().getOneOrGenerateForUser(userUUID).thenApplyAsync(optionalMember ->
            optionalMember.isPresent()
                ? stringResult.builder()
                .append(pluginInfo.getPrefix())
                .blue().append("Username: ").aqua().append(userService.getUserName(userUUID).orElse("null"))
                .build()
                : stringResult.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Could not find user ", userService.getUserName(userUUID).orElse(userUUID.toString()))
                .build());
    }
}
