/*
 *   OnTime - AnvilPowered
 *   Copyright (C) 2020 Cableguy20
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.ontime.sponge.plugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import org.anvilpowered.ontime.sponge.module.SpongeModule;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.anvilpowered.ontime.api.tasks.SyncTaskService;
import org.anvilpowered.ontime.common.plugin.MSOnTimePluginInfo;
import org.anvilpowered.ontime.sponge.commands.OnTimeCommandManager;
import org.anvilpowered.ontime.sponge.listeners.PlayerListener;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.sponge.module.ApiSpongeModule;

@Plugin(
    id = MSOnTimePluginInfo.id,
    name = MSOnTimePluginInfo.name,
    version = MSOnTimePluginInfo.version,
    description = MSOnTimePluginInfo.description,
    authors = {"Cableguy20", "STG_Allen"},
    url = MSOnTimePluginInfo.url,
    dependencies = @Dependency(id = "mscore")
)
public class MSOnTime {

    @Override
    public String toString() {
        return MSOnTimePluginInfo.id;
    }

    @Inject
    private Injector spongeRootInjector;

    @Inject
    private Logger logger;

    public static MSOnTime plugin = null;
    private Injector injector = null;
    private PluginInfo<Text> pluginInfo = null;

    private boolean alreadyLoadedOnce = false;

    @Listener
    public void onServerInitialization(GameInitializationEvent event) {
        plugin = this;
        injector = spongeRootInjector.createChildInjector(new SpongeModule(), new ApiSpongeModule());
        pluginInfo = injector.getInstance(Key.get(new TypeLiteral<PluginInfo<Text>>() {
        }));
        Sponge.getServer().getConsole().sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.AQUA, "Loading..."));
        initCommands();
        injector.getInstance(SyncTaskService.class);
        Sponge.getEventManager().registerListeners(this, injector.getInstance(PlayerListener.class));
        loadRegistry();
        Sponge.getServer().getConsole().sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.AQUA, "Done"));
    }

    @Listener
    public void reload(GameReloadEvent event) {
        loadRegistry();
        logger.info("Reloaded successfully!");
    }

    @Listener
    public void stop(GameStoppingEvent event) {
        Sponge.getServer().getConsole().sendMessage(Text.of(pluginInfo.getPrefix(), TextColors.AQUA, "Stopping"));
    }

    private void loadRegistry() {
        injector.getInstance(Registry.class).load();
    }

    private void initCommands() {
        if (!alreadyLoadedOnce) {
            injector.getInstance(OnTimeCommandManager.class).register(this);
            alreadyLoadedOnce = true;
        }
    }
}
