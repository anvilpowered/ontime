package rocks.milspecsg.msontime;

import com.google.inject.Inject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import rocks.milspecsg.msrepository.api.util.PluginInfo;

public class PluginMessages {

    @Inject
    private static PluginInfo<Text> pluginInfo;

    public static Text insufficientPermissions = Text.of(pluginInfo.getPrefix(), TextColors.RED, "Insufficient Permissions!");

}
