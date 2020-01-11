package rocks.milspecsg.msontime.model.core.member;

import rocks.milspecsg.msrepository.model.data.dbo.ObjectWithId;

import java.util.UUID;

public interface Member<TKey> extends ObjectWithId<TKey> {

    UUID getUserUUID();
    void setUserUUID(UUID userUUID);

    int getPlayTime();
    void setPlayTime(int playTime);

    int getBonusTime();
    void setBonusTime(int bonusTime);
}
