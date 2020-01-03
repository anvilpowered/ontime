package rocks.milspecsg.msontime.commands;

import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnTimeCommandManager implements CommandManager {

    @Inject
    OnTimeBaseCommand onTimeBaseCommand;

    @Inject
    OnTimeCheckCommand onTimeCheckCommand;

    @Override
    public void register(Object plugin) {
        Map<List<String>, CommandSpec> subCommands = new HashMap<>();

        subCommands.put(Arrays.asList("check", "c", "info", "i"), CommandSpec.builder()
            .description(Text.of("Check play time"))
            .arguments(
                GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))))
            )
            .executor(onTimeCheckCommand)
            .build()
        );

        CommandSpec mainCommand = CommandSpec.builder()
            .description(Text.of("Base command"))
            .executor(onTimeBaseCommand)
            .children(subCommands)
            .build();

        Sponge.getCommandManager().register(plugin, mainCommand, "msontime", "ontime", "ot");
    }
}
