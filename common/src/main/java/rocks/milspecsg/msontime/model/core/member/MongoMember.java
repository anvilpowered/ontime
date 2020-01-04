package rocks.milspecsg.msontime.model.core.member;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import rocks.milspecsg.msrepository.model.data.dbo.MongoDbo;

import java.util.UUID;

@Entity("members")
public class MongoMember extends MongoDbo implements Member<ObjectId> {

    private UUID userUUID;
    private int playTime;

    @Override
    public UUID getUserUUID() {
        return userUUID;
    }

    @Override
    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    @Override
    public int getPlayTime() {
        return playTime;
    }

    @Override
    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }
}
