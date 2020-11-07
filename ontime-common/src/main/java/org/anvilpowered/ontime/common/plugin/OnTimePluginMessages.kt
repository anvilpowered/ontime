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

package org.anvilpowered.ontime.common.plugin

import com.google.inject.Inject
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.ontime.api.plugin.PluginMessages

class OnTimePluginMessages<TString, TCommandSource> : PluginMessages<TString> {

    @Inject
    private lateinit var pluginInfo: PluginInfo<TString>

    @Inject
    private lateinit var textService: TextService<TString, TCommandSource>

    private val noPermissionText: TString by lazy {
        textService.builder()
            .append(pluginInfo.prefix)
            .red().append("You do not have permission for this command!")
            .build()
    }

    override fun getNoPermission(): TString = noPermissionText
}
