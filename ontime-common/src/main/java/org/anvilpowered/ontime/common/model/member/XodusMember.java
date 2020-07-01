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

import jetbrains.exodus.entitystore.Entity;
import jetbrains.exodus.entitystore.EntityId;
import org.anvilpowered.anvil.api.datastore.XodusEntity;
import org.anvilpowered.anvil.base.model.XodusDbo;
import org.anvilpowered.ontime.api.model.member.Member;

import java.util.UUID;

@XodusEntity
public class XodusMember extends XodusDbo implements Member<EntityId> {

    private UUID userUUID;
    private long playTime;
    private long bonusTime;

    @Override
    public UUID getUserUUID() {
        return userUUID;
    }

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

    @Override
    public Entity writeTo(Entity object) {
        super.writeTo(object);
        object.setProperty("userUUID", userUUID.toString());
        object.setProperty("playTime", playTime);
        object.setProperty("bonusTime", bonusTime);
        return object;
    }

    @Override
    public void readFrom(Entity object) {
        super.readFrom(object);
        Comparable<?> userUUID = object.getProperty("userUUID");
        if (userUUID instanceof String) {
            this.userUUID = UUID.fromString((String) userUUID);
        }
        Comparable<?> playTime = object.getProperty("playTime");
        if (playTime instanceof Long) {
            this.playTime = (Long) playTime;
        }
        Comparable<?> bonusTime = object.getProperty("bonusTime");
        if (bonusTime instanceof Long) {
            this.bonusTime = (Long) bonusTime;
        }
    }
}
