package rocks.milspecsg.msontime.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class OnTimeBaseCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) {

        return CommandResult.success();
    }
}
