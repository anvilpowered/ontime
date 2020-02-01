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

package rocks.milspecsg.msontime.api.member.repository;

import rocks.milspecsg.msontime.api.model.member.Member;
import rocks.milspecsg.msrepository.api.repository.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MemberRepository<
    TKey,
    TDataStore>
    extends Repository<TKey, Member<TKey>, TDataStore> {

    CompletableFuture<Optional<Member<TKey>>> getOneOrGenerateForUser(UUID userUUID);

    CompletableFuture<Optional<Member<TKey>>> generateUserFromConfig(UUID userUUID, int playTime);

    CompletableFuture<Optional<Member<TKey>>> getOneForUser(UUID userUUID);

    CompletableFuture<Boolean> addMinuteForUser(UUID userUUID);

    CompletableFuture<Boolean> setBonusTimeForUser(UUID userUUID, int bonusTime);

    CompletableFuture<Boolean> addBonusTimeForUser(UUID userUUID, int bonusTime);
}
