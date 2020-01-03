package rocks.milspecsg.msontime;

import com.google.inject.Inject;
import rocks.milspecsg.msrepository.PluginInfo;
import rocks.milspecsg.msrepository.api.tools.resultbuilder.StringResult;

public class MSOnTimePluginInfo<TString, TCommandSource> implements PluginInfo<TString> {
    public static final String id = "msontime";
    public static final String name = "MSOnTime";
    public static final String version = "$modVersion";
    public static final String description = "Playtime tracker";
    public static final String url = "https://github.com/MilSpecSG/MSOnTime";
    public static final String authors = "Cableguy20";
    public TString pluginPrefix;

    @Inject
    public void setPluginPrefix(StringResult<TString, TCommandSource> stringResult) {
        pluginPrefix = stringResult.builder().aqua().append("[", name, "] ").build();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getURL() {
        return url;
    }

    @Override
    public String getAuthors() {
        return authors;
    }

    public TString getPrefix() {
        return pluginPrefix;
    }
}
