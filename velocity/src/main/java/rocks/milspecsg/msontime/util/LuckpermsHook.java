package rocks.milspecsg.msontime.util;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.Player;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class LuckpermsHook {

    private LuckPerms luckperms;

    @Inject
    public LuckpermsHook(LuckPerms luckPerms) {
        this.luckperms = luckPerms;
    }

    public Optional<CachedMetaData> getMetaData(Player player) {
        UUID playerUUID = player.getUniqueId();
        User tempUser = luckperms.getUserManager().getUser(playerUUID);

        return Optional.ofNullable(tempUser).map(User::getCachedData)
            .map(data -> data.getMetaData(getQueryOptions(Optional.of(tempUser))));
    }

    public String getRank(Player player) {
        User tempUser = luckperms.getUserManager().getUser(player.getUniqueId());
        return tempUser.getPrimaryGroup();
    }

    private QueryOptions getQueryOptions(Optional<User> user) {
        return user.flatMap(luckperms.getContextManager()::getQueryOptions)
            .orElseGet(luckperms.getContextManager()::getStaticQueryOptions);
    }
}
