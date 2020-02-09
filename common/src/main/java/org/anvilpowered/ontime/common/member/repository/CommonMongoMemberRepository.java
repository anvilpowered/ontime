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

package org.anvilpowered.ontime.common.member.repository;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.anvilpowered.ontime.api.member.repository.MongoMemberRepository;
import org.anvilpowered.ontime.api.model.member.Member;
import org.anvilpowered.anvil.api.datastore.DataStoreContext;
import org.anvilpowered.anvil.common.repository.CommonMongoRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonMongoMemberRepository
    extends CommonMemberRepository<ObjectId, Datastore>
    implements CommonMongoRepository<Member<ObjectId>>,
    MongoMemberRepository {

    @Inject
    public CommonMongoMemberRepository(DataStoreContext<ObjectId, Datastore> dataStoreContext) {
        super(dataStoreContext);
    }

    @Override
    public CompletableFuture<Optional<Member<ObjectId>>> getOneForUser(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(asQuery(userUUID).get()));
    }

    @Override
    public CompletableFuture<Boolean> addMinute(Query<Member<ObjectId>> query) {
        return update(query, inc("playTime"));
    }

    @Override
    public CompletableFuture<Boolean> addBonusTime(Query<Member<ObjectId>> query, int time) {
        return update(query, inc("bonusTime", time));
    }

    @Override
    public CompletableFuture<Boolean> setBonusTime(Query<Member<ObjectId>> query, int time) {
        return update(query, set("bonusTime", time));
    }

    @Override
    public CompletableFuture<Boolean> setTotalTime(Query<Member<ObjectId>> query, int time) {
        return CompletableFuture.supplyAsync(() -> {
            Member<ObjectId> member = query.project("playTime", true).get();
            if (member == null) {
                return false;
            }
            return getDataStoreContext()
                .getDataStore()
                .update(query, set("bonusTime", time - member.getPlayTime()))
                .getUpdatedCount() > 0;
        });
    }

    @Override
    public CompletableFuture<Boolean> addMinuteForUser(UUID userUUID) {
        return addMinute(asQuery(userUUID));
    }

    @Override
    public CompletableFuture<Boolean> addBonusTimeForUser(UUID userUUID, int time) {
        return addBonusTime(asQuery(userUUID), time);
    }

    @Override
    public CompletableFuture<Boolean> setBonusTimeForUser(UUID userUUID, int bonusTime) {
        return setBonusTime(asQuery(userUUID), bonusTime);
    }

    @Override
    public CompletableFuture<Boolean> setTotalTimeForUser(UUID userUUID, int time) {
        return setTotalTime(asQuery(userUUID), time);
    }

    @Override
    public Query<Member<ObjectId>> asQuery(UUID userUUID) {
        return asQuery().field("userUUID").equal(userUUID);
    }
}
