package rocks.milspecsg.msontime.api.member;

import rocks.milspecsg.msontime.api.member.repository.MemberRepository;
import rocks.milspecsg.msrepository.api.manager.Manager;

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

    void setBonusTime(UUID userUUID, int bonusTime);

    CompletableFuture<TString> addBonusTime(UUID userUUID, int bonusTime);

    CompletableFuture<TString> getNextGroup(UUID userUUID);
}
