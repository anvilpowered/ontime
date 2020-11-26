/*
 *   OnTime - AnvilPowered
 *   Copyright (C) 2020
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.ontime.common.model.member;

import dev.morphia.annotations.Entity;
import org.anvilpowered.anvil.base.model.MongoDbo;
import org.anvilpowered.ontime.api.model.member.Member;
import org.bson.types.ObjectId;

import java.util.UUID;

@Entity("members")
public class MongoMember extends MongoDbo implements Member<ObjectId> {

    private UUID userUUID;
    private long playTime;
    private long bonusTime;

    @Override
    public UUID getUserUUID() {
        return userUUID;
    }

    @Override
    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    @Override
    public long getPlayTime() {
        return playTime;
    }

    @Override
    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    @Override
    public long getBonusTime() {
        return bonusTime;
    }

    @Override
    public void setBonusTime(long bonusTime) {
        this.bonusTime = bonusTime;
    }

}
