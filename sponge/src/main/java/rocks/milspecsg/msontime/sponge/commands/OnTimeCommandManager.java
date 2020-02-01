package rocks.milspecsg.msontime.sponge.commands;

import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnTimeCommandManager {

    @Inject
    OnTimeBaseCommand onTimeBaseCommand;

    @Inject
    OnTimeCheckCommand onTimeCheckCommand;

    @Inject
    OnTimeSetCommand onTimeSetCommand;

    @Inject
    OnTimeAddCommand onTimeAddCommand;

    @Inject
    OnTimeImportCommand onTimeImportCommand;

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

        subCommands.put(Arrays.asList("set"), CommandSpec.builder()
            .description(Text.of("Set bonus playtime"))
            .arguments(
                GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                GenericArguments.integer(Text.of("time"))
            )
            .executor(onTimeSetCommand)
            .build()
        );
        subCommands.put(Arrays.asList("add"), CommandSpec.builder()
            .description(Text.of("Add bonus time to a player"))
            .arguments(
                GenericArguments.onlyOne(GenericArguments.user(Text.of("user"))),
                GenericArguments.integer(Text.of("time"))
            )
            .executor(onTimeAddCommand)
            .build()
        );

        subCommands.put(Arrays.asList("convert-from-rankup"), CommandSpec.builder()
            .description(Text.of("Print out all uuids from playerstats.conf"))
            .arguments(GenericArguments.string(Text.of("path")))
            .executor(onTimeImportCommand)
            .build());

        CommandSpec mainCommand = CommandSpec.builder()
            .description(Text.of("Base command"))
            .executor(onTimeBaseCommand)
            .children(subCommands)
            .build();

        Sponge.getCommandManager().register(plugin, mainCommand, "msontime", "ontime", "ot", "playtime");
    }
}
