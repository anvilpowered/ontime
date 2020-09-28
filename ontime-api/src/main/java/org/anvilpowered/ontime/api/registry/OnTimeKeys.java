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

package org.anvilpowered.ontime.api.registry;

import com.google.common.reflect.TypeToken;
import org.anvilpowered.anvil.api.registry.Key;
import org.anvilpowered.anvil.api.registry.Keys;
import org.anvilpowered.anvil.api.registry.TypeTokens;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public final class OnTimeKeys {

    public static final TypeToken<Map<String, Integer>> MAP_STRING_INT = new TypeToken<Map<String, Integer>>() {
    };

    public static final Key<Map<String, Integer>> RANKS =
        Key.builder(MAP_STRING_INT)
            .name("RANKS")
            .fallback(new HashMap<>())
            .build();
    public static final Key<String> CHECK_PERMISSION =
        Key.builder(TypeTokens.STRING)
            .name("CHECK_PERMISSION")
            .fallback("ontime.user.check")
            .build();
    public static final Key<String> CHECK_EXTENDED_PERMISSION =
        Key.builder(TypeTokens.STRING)
            .name("CHECK_EXTENDED_PERMISSION")
            .fallback("ontime.admin.check")
            .build();
    public static final Key<String> EDIT_PERMISSION =
        Key.builder(TypeTokens.STRING)
            .name("EDIT_PERMISSION")
            .fallback("ontime.admin.edit")
            .build();
    public static final Key<String> IMPORT_PERMISSION =
        Key.builder(TypeTokens.STRING)
            .name("IMPORT_PERMISSION")
            .fallback("ontime.admin.import")
            .build();

    static {
        Keys.startRegistration("ontime")
            .register(RANKS)
            .register(CHECK_PERMISSION)
            .register(CHECK_EXTENDED_PERMISSION)
            .register(EDIT_PERMISSION)
            .register(IMPORT_PERMISSION)
        ;
    }

    private OnTimeKeys() {
        throw new AssertionError("**boss music** No instance for you!");
    }
}
