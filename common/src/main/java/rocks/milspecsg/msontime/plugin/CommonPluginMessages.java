package rocks.milspecsg.msontime.plugin;


import com.google.inject.Inject;
import rocks.milspecsg.msontime.api.plugin.PluginMessages;
import rocks.milspecsg.msrepository.api.util.StringResult;

public class CommonPluginMessages<TString, TCommandSource> implements PluginMessages<TString> {

    @Inject
    StringResult<TString, TCommandSource> stringResult;

    @Inject
    MSOnTimePluginInfo<TString, TCommandSource> pluginInfo;

    @Override
    public TString getInsufficientPermission() {
        return stringResult.builder()
            .append(pluginInfo.pluginPrefix)
            .red().append("Insufficient Permissions!")
            .build();
    }

    @Override
    public TString getNotEnoughArgs() {
        return stringResult.builder()
            .append(pluginInfo.pluginPrefix)
            .yellow().append("Not enough arguments!")
            .build();
    }
}
