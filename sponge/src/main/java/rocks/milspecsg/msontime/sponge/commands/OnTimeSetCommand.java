/*
 *     MSOnTime - MilSpecSG
 *     Copyright (C) 2019 Cableguy20
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

package rocks.milspecsg.msontime.sponge.commands;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msontime.api.member.MemberManager;
import rocks.milspecsg.msrepository.api.util.PluginInfo;

public class OnTimeSetCommand implements CommandExecutor {

    @Inject
    private MemberManager<Text> memberManager;

    @Inject
    private PluginInfo<Text> pluginInfo;

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException {
        memberManager.setTotalTime(
            context.<User>getOne(Text.of("user"))
                .orElseThrow(() -> new CommandException(Text.of(pluginInfo.getPrefix(), "Missing user")))
                .getUniqueId(),
            context.<Integer>getOne(Text.of("time"))
                .orElseThrow(() -> new CommandException(Text.of(pluginInfo.getPrefix(), "Missing time")))
        ).thenAcceptAsync(source::sendMessage);
        return CommandResult.success();
    }
}
