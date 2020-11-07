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
package org.anvilpowered.ontime.sponge.command

import com.google.inject.Inject
import com.google.inject.Singleton
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.ontime.api.registry.OnTimeKeys
import org.anvilpowered.ontime.common.command.CommonOnTimeCommandNode
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandCallable
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.text.Text
import java.util.HashMap

@Singleton
class SpongeOnTimeCommandNode @Inject constructor(
    registry: Registry
) : CommonOnTimeCommandNode<CommandExecutor, CommandSource>(registry) {

    @Inject
    private lateinit var addCommand: SpongeAddCommand

    @Inject
    private lateinit var onTimeCheckCommand: SpongeCheckCommand

    @Inject
    private lateinit var importCommand: SpongeImportCommand

    @Inject
    private lateinit var setBonusCommand: SpongeSetBonusCommand

    @Inject
    private lateinit var setTotalCommand: SpongeSetTotalCommand

    override fun loadCommands() {
        val subCommands: MutableMap<List<String>, CommandCallable> = HashMap()
        subCommands[ADD_ALIAS] = addCommand
        subCommands[CHECK_ALIAS] = onTimeCheckCommand
        subCommands[IMPORT_ALIAS] = CommandSpec.builder()
            .description(Text.of(IMPORT_DESCRIPTION))
            .permission(registry.getOrDefault(OnTimeKeys.IMPORT_PERMISSION))
            .arguments(GenericArguments.remainingJoinedStrings(Text.of("path")))
            .executor(importCommand)
            .build()
        subCommands[SET_BONUS_ALIAS] = setBonusCommand
        subCommands[SET_TOTAL_ALIAS] = setTotalCommand
        subCommands[HELP_ALIAS] = CommandSpec.builder()
            .description(Text.of(HELP_COMMAND))
            .executor(commandService.generateHelpCommand(this))
            .build()
        subCommands[VERSION_ALIAS] = CommandSpec.builder()
            .description(Text.of(VERSION_DESCRIPTION))
            .executor(commandService.generateVersionCommand(HELP_COMMAND))
            .build()
        val command = CommandSpec.builder()
            .description(Text.of(ROOT_DESCRIPTION))
            .executor(commandService.generateRootCommand(HELP_COMMAND))
            .children(subCommands)
            .build()
        Sponge.getCommandManager()
            .register(environment.plugin, command, OnTimePluginInfo.id, "ot")
    }
}
