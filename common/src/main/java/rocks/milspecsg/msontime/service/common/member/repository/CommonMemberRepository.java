package rocks.milspecsg.msontime.service.common.member.repository;

import rocks.milspecsg.msontime.api.member.repository.MemberRepository;
import rocks.milspecsg.msontime.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.datastore.DataStoreConfig;
import rocks.milspecsg.msrepository.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.service.common.repository.CommonRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class CommonMemberRepository<
    TKey,
    TDataStore,
    TDataStoreConfig extends DataStoreConfig>
    extends CommonRepository<TKey, Member<TKey>, CacheService<TKey, Member<TKey>, TDataStore, TDataStoreConfig>, TDataStore, TDataStoreConfig>
    implements MemberRepository<TKey, TDataStore, TDataStoreConfig> {

    protected CommonMemberRepository(DataStoreContext<TKey, TDataStore, TDataStoreConfig> dataStoreContext) {
        super(dataStoreContext);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Member<TKey>> getTClass() {
        return (Class<Member<TKey>>) getDataStoreContext().getEntityClassUnsafe("member");
    }

    @Override
    public CompletableFuture<Optional<Member<TKey>>> getOneOrGenerateForUser(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<Member<TKey>> optionalMember = getOneForUser(userUUID).join();
                if (optionalMember.isPresent()) return optionalMember;
                // if there isn't one already, create a new one
                Member<TKey> member = generateEmpty();
                member.setUserUUID(userUUID);
                return insertOne(member).join();
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }
}
