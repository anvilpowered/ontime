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

package org.anvilpowered.ontime.common.member;

import org.anvilpowered.anvil.base.datastore.BaseMongoRepository;
import org.anvilpowered.ontime.api.member.MongoMemberRepository;
import org.anvilpowered.ontime.api.model.member.Member;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonMongoMemberRepository
    extends CommonMemberRepository<ObjectId, Datastore>
    implements BaseMongoRepository<Member<ObjectId>>,
    MongoMemberRepository {

    @Override
    public CompletableFuture<Optional<Member<ObjectId>>> getOneForUser(UUID userUUID) {
        return getOne(asQuery(userUUID));
    }

    @Override
    public CompletableFuture<Boolean> addTime(Query<Member<ObjectId>> query, long time) {
        return update(query, inc("playTime", time));
    }

    @Override
    public CompletableFuture<Boolean> addBonusTime(Query<Member<ObjectId>> query, long time) {
        return update(query, inc("bonusTime", time));
    }

    @Override
    public CompletableFuture<Boolean> setBonusTime(Query<Member<ObjectId>> query, long time) {
        return update(query, set("bonusTime", time));
    }

    @Override
    public CompletableFuture<Boolean> setTotalTime(Query<Member<ObjectId>> query, long time) {
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
    public CompletableFuture<Boolean> addTimeForUser(UUID userUUID, long time) {
        return addTime(asQuery(userUUID), time);
    }

    @Override
    public CompletableFuture<Boolean> addBonusTimeForUser(UUID userUUID, long time) {
        return addBonusTime(asQuery(userUUID), time);
    }

    @Override
    public CompletableFuture<Boolean> setBonusTimeForUser(UUID userUUID, long time) {
        return setBonusTime(asQuery(userUUID), time);
    }

    @Override
    public CompletableFuture<Boolean> setTotalTimeForUser(UUID userUUID, long time) {
        return setTotalTime(asQuery(userUUID), time);
    }

    @Override
    public Query<Member<ObjectId>> asQuery(UUID userUUID) {
        return asQuery().field("userUUID").equal(userUUID);
    }
}
