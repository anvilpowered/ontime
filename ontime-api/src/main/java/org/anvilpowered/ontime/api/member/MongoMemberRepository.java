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

package org.anvilpowered.ontime.api.member;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import org.anvilpowered.ontime.api.model.member.Member;
import org.bson.types.ObjectId;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MongoMemberRepository extends MemberRepository<ObjectId, Datastore> {

    CompletableFuture<Boolean> addTime(Query<Member<ObjectId>> query, long time);

    CompletableFuture<Boolean> addBonusTime(Query<Member<ObjectId>> query, long time);

    CompletableFuture<Boolean> setBonusTime(Query<Member<ObjectId>> query, long time);

    CompletableFuture<Boolean> setTotalTime(Query<Member<ObjectId>> query, long time);

    Query<Member<ObjectId>> asQuery(UUID userUUID);
}
