package rocks.milspecsg.msontime.sponge.commands;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msontime.api.member.MemberManager;
import rocks.milspecsg.msrepository.api.util.PluginInfo;

import java.util.Optional;

public class OnTimeSetCommand implements CommandExecutor {

    @Inject
    MemberManager<Text> memberManager;

    @Inject
    PluginInfo<Text> pluginInfo;

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) {
        Optional<User> optionalUser = context.getOne(Text.of("user"));
        Optional<Integer> optionalTime = context.getOne("time");

        if(source.hasPermission(PluginPermissions.SET)) {
            if (optionalUser.isPresent() && optionalTime.isPresent()) {
                memberManager.setBonusTime(optionalUser.get().getUniqueId(), optionalTime.get());
            } else if (source instanceof Player) {
                memberManager.info(((Player) source).getUniqueId()).thenAcceptAsync(source::sendMessage);
            } else {
                source.sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.RED, "Invalid player name!"));
            }
        } else {
            source.sendMessage(PluginMessages.insufficientPermissions);
        }
        return CommandResult.success();
    }
}
