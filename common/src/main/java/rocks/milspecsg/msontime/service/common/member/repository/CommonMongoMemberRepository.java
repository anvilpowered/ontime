package rocks.milspecsg.msontime.service.common.member.repository;

import com.google.inject.Inject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msontime.api.member.repository.MongoMemberRepository;
import rocks.milspecsg.msontime.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.datastore.mongodb.MongoConfig;
import rocks.milspecsg.msrepository.service.common.repository.CommonMongoRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonMongoMemberRepository
        extends CommonMemberRepository<ObjectId, Datastore, MongoConfig>
        implements CommonMongoRepository<Member<ObjectId>, CacheService<ObjectId, Member<ObjectId>, Datastore, MongoConfig>>,
        MongoMemberRepository {

    @Inject
    public CommonMongoMemberRepository(DataStoreContext<ObjectId, Datastore, MongoConfig> dataStoreContext) {
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
            if (updateOperations
                    .map(memberUpdateOperations -> getDataStoreContext().getDataStore()
                            .map(datastore -> datastore.update(query, memberUpdateOperations).getUpdatedCount() > 0 ).orElse(false)
                    ).orElse(false)) {
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> addBonusTime(Query<Member<ObjectId>> query, int bonusTime) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<UpdateOperations<Member<ObjectId>>> updateOperations = createUpdateOperations().map(u -> u.set("bonusTime", query.get().getBonusTime() + bonusTime));
            if(updateOperations.map(memberUpdateOperations -> getDataStoreContext().getDataStore()
            .map(datastore -> datastore.update(query, memberUpdateOperations).getUpdatedCount() > 0).orElse(false)).orElse(false)) {
                return true;
            }
            return false;
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
