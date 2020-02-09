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

package org.anvilpowered.ontime.api.member;

import org.anvilpowered.ontime.api.member.repository.MemberRepository;
import org.anvilpowered.anvil.api.manager.Manager;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MemberManager<TString> extends Manager<MemberRepository<?, ?>> {

    @Override
    default String getDefaultIdentifierSingularUpper() {
        return "Member";
    }

    @Override
    default String getDefaultIdentifierPluralUpper() {
        return "Members";
    }

    @Override
    default String getDefaultIdentifierSingularLower() {
        return "member";
    }

    @Override
    default String getDefaultIdentifierPluralLower() {
        return "members";
    }

    CompletableFuture<TString> info(UUID userUUID);

    CompletableFuture<TString> infoExtended(UUID userUUID);

    CompletableFuture<Optional<String>> sync(UUID userUUID);

    CompletableFuture<TString> addBonusTime(UUID userUUID, int time);

    CompletableFuture<TString> setBonusTime(UUID userUUID, int time);

    CompletableFuture<TString> setTotalTime(UUID userUUID, int time);

    CompletableFuture<TString> getNextGroup(UUID userUUID);
}
