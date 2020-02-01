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

package rocks.milspecsg.msontime.common.model.member;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import rocks.milspecsg.msontime.api.model.member.Member;
import rocks.milspecsg.msrepository.common.model.MongoDbo;

import java.util.UUID;

@Entity("members")
public class MongoMember extends MongoDbo implements Member<ObjectId> {

    private UUID userUUID;
    private int playTime;
    private int bonusTime;

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

    @Override
    public int getBonusTime() {
        return bonusTime;
    }

    @Override
    public void setBonusTime(int bonusTime) {
        this.bonusTime = bonusTime;
    }

}
