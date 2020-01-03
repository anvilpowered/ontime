package rocks.milspecsg.msontime.api.member.repository;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import rocks.milspecsg.msontime.model.core.member.Member;
import rocks.milspecsg.msrepository.datastore.mongodb.MongoConfig;

import java.util.Optional;
import java.util.UUID;

public interface MongoMemberRepository extends MemberRepository<ObjectId, Datastore, MongoConfig> {

    Optional<Query<Member<ObjectId>>> asQuery(UUID userUUID);
}
