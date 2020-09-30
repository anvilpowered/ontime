/*
 *   OnTime - AnvilPowered
 *   Copyright (C) 2020
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.anvilpowered.ontime.common.registry

import com.google.inject.Inject
import com.google.inject.Singleton
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.anvilpowered.anvil.api.registry.Keys
import org.anvilpowered.anvil.base.registry.BaseConfigurationService
import org.anvilpowered.ontime.api.registry.OnTimeKeys
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo
import java.util.HashMap

@Singleton
open class CommonConfigurationService @Inject constructor(
    configLoader: ConfigurationLoader<CommentedConfigurationNode>
) : BaseConfigurationService(configLoader) {
    init {
        withMongoDB()
        setDefault(Keys.DATA_DIRECTORY, OnTimePluginInfo.id)
        setDefault(Keys.MONGODB_DBNAME, OnTimePluginInfo.id)
        val defaultCommandMap: MutableMap<String, List<String>> = HashMap()
        defaultCommandMap[".*"] = listOf(
            "say %player% has advanced to %rank% after playing for %time%",
            "give %player% iron_ingot 1"
        )
        defaultCommandMap["trusted"] = listOf(
            "say %player% is the best"
        )
        setDefault(OnTimeKeys.COMMANDS, defaultCommandMap)
        val defaultRankMap: MutableMap<String, Int> = HashMap()
        defaultRankMap["noob"] = 0
        defaultRankMap["player"] = 600
        defaultRankMap["trusted"] = 1800
        setDefault(OnTimeKeys.RANKS, defaultRankMap)
        setName(OnTimeKeys.COMMANDS, "commands")
        setName(OnTimeKeys.RANKS, "ranks")
        setDescription(OnTimeKeys.COMMANDS, """
Commands to run after a player has received a rank. The nodes are compiled as a regex and compared with the
new rank name. The commands for every matching regex will run (not just the commands for the first regex that matches).
Available placeholders:
 - %player% : The player's username
 - %rank% : The player's new rank
 - %time% : The time requirement of the player's new rank
Note: ".*" is the regex that matches everything (commands under this will run for every rank up).
For more information on regex, visit https://regexr.com/
""")
        setDescription(OnTimeKeys.RANKS, "\nPlayer ranks and their time requirement in seconds.")
    }
}
