package rocks.milspecsg.msontime.api.member.repository;

import rocks.milspecsg.msontime.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
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
