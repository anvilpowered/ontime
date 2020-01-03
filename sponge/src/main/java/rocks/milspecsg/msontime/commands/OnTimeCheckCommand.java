package rocks.milspecsg.msontime.commands;

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
import rocks.milspecsg.msrepository.PluginInfo;

import java.util.Optional;

public class OnTimeCheckCommand implements CommandExecutor {

    @Inject
    MemberManager<Text> memberManager;

    @Inject
    PluginInfo<Text> pluginInfo;

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) {
        Optional<User> optionalUser = context.getOne(Text.of("user"));

        if (optionalUser.isPresent()) {
            memberManager.info(optionalUser.get().getUniqueId()).thenAcceptAsync(source::sendMessage);
        } else if (source instanceof Player) {
            memberManager.info(((Player) source).getUniqueId()).thenAcceptAsync(source::sendMessage);
        } else {
            source.sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.RED, "You must supply a player name or run this command as a player"));
        }

        return CommandResult.success();
    }
}
