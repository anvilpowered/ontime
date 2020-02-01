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

package rocks.milspecsg.msontime.common.member.repository;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msontime.api.member.repository.MongoMemberRepository;
import rocks.milspecsg.msontime.api.model.member.Member;
import rocks.milspecsg.msrepository.api.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.common.repository.CommonMongoRepository;

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
        return CompletableFuture.supplyAsync(() -> asQuery(userUUID).map(QueryResults::get));
    }

    @Override
    public CompletableFuture<Boolean> addMinute(Query<Member<ObjectId>> query) {
        return CompletableFuture.supplyAsync(() ->
            getDataStoreContext().getDataStore()
                .flatMap(dataStore -> inc("playTime")
                    .map(u -> dataStore.update(query, u).getUpdatedCount() > 0)
                ).orElse(false)
        ).exceptionally(e -> false);
    }

    @Override
    public CompletableFuture<Boolean> setBonusTime(Query<Member<ObjectId>> query, int bonusTime) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<UpdateOperations<Member<ObjectId>>> updateOperations = createUpdateOperations().map(u -> u.set("bonusTime", bonusTime));
            return updateOperations
                .map(memberUpdateOperations -> getDataStoreContext().getDataStore()
                    .map(datastore -> datastore.update(query, memberUpdateOperations).getUpdatedCount() > 0).orElse(false)
                ).orElse(false);
        });
    }

    @Override
    public CompletableFuture<Boolean> addBonusTime(Query<Member<ObjectId>> query, int bonusTime) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<UpdateOperations<Member<ObjectId>>> updateOperations = createUpdateOperations().map(u -> u.set("bonusTime", query.get().getBonusTime() + bonusTime));
            return updateOperations.map(memberUpdateOperations -> getDataStoreContext().getDataStore()
                .map(datastore -> datastore.update(query, memberUpdateOperations).getUpdatedCount() > 0).orElse(false)).orElse(false);
        });
    }

    @Override
    public CompletableFuture<Boolean> addMinuteForUser(UUID userUUID) {
        return asQuery(userUUID).map(this::addMinute).orElse(CompletableFuture.completedFuture(false));
    }

    @Override
    public CompletableFuture<Boolean> setBonusTimeForUser(UUID userUUID, int bonusTime) {
        return asQuery(userUUID).map(q -> setBonusTime(q, bonusTime)).orElse(CompletableFuture.completedFuture(false));
    }

    @Override
    public CompletableFuture<Boolean> addBonusTimeForUser(UUID userUUID, int bonusTime) {
        return asQuery(userUUID).map(q -> addBonusTime(q, bonusTime)).orElse(CompletableFuture.completedFuture(false));
    }

    @Override
    public Optional<Query<Member<ObjectId>>> asQuery(UUID userUUID) {
        return asQuery().map(q -> q.field("userUUID").equal(userUUID));
    }
}
