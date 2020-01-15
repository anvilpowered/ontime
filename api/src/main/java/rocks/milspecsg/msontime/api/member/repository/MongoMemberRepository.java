package rocks.milspecsg.msontime.api.member.repository;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import rocks.milspecsg.msontime.model.core.member.Member;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MongoMemberRepository extends MemberRepository<ObjectId, Datastore> {

    CompletableFuture<Boolean> addMinute(Query<Member<ObjectId>> query);

    CompletableFuture<Boolean> setBonusTime(Query<Member<ObjectId>> query, int bonusTime);

    CompletableFuture<Boolean> addBonusTime(Query<Member<ObjectId>> query, int bonusTime);

    Optional<Query<Member<ObjectId>>> asQuery(UUID userUUID);
}
