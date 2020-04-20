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

package org.anvilpowered.ontime.sponge.command;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.ontime.api.data.key.OnTimeKeys;
import org.anvilpowered.ontime.api.member.MemberManager;
import org.anvilpowered.ontime.common.command.CommonOnTimeCheckCommand;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class SpongeOnTimeCheckCommand
    extends CommonOnTimeCheckCommand<User, Player, Subject, Text, CommandSource>
    implements CommandExecutor {

    @Inject
    private MemberManager<Text> memberManager;

    @Inject
    private Registry registry;

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) {
        Optional<User> optionalUser = context.getOne(Text.of("user"));
        if (optionalUser.isPresent()) {
            memberManager.infoExtended(optionalUser.get().getUniqueId()).thenAcceptAsync(source::sendMessage);
        } else if (source instanceof Player) {
            if (source.hasPermission(registry.getOrDefault(OnTimeKeys.CHECK_EXTENDED_PERMISSION))) {
                memberManager.infoExtended(((Player) source).getUniqueId()).thenAcceptAsync(source::sendMessage);
            } else {
                memberManager.info(((Player) source).getUniqueId()).thenAcceptAsync(source::sendMessage);
            }
        } else {
            sendError(source);
        }
        return CommandResult.success();
    }
}
