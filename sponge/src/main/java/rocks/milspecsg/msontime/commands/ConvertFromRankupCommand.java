package rocks.milspecsg.msontime.commands;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msontime.api.util.DataImportService;
import rocks.milspecsg.msrepository.api.util.PluginInfo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ConvertFromRankupCommand implements CommandExecutor {
    @Inject
    private DataImportService importService;

    @Inject
    PluginInfo<Text> pluginInfo;

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        Optional<String> optionalPath = context.getOne(Text.of("path"));
        if (optionalPath.isPresent()) {
            String inputPath = optionalPath.get();
            Path path = Paths.get(inputPath);

            if (path.toFile().exists()) {
                source.sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.GREEN, "Starting Rankup import!"));
                importService.importData(path);
            } else {
                source.sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.YELLOW, "Could not find the specified file." +
                    "\nDid you mean:", TextColors.GOLD, "\"config/rankup/playerstats.conf\" ?"));
            }
        } else {
            throw new CommandException(Text.of("Please specify a path!"));
        }
        return CommandResult.success();
    }
}
