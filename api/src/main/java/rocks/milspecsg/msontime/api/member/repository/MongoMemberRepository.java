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

package rocks.milspecsg.msontime.api.member.repository;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import rocks.milspecsg.msontime.api.model.member.Member;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MongoMemberRepository extends MemberRepository<ObjectId, Datastore> {

    CompletableFuture<Boolean> addMinute(Query<Member<ObjectId>> query);

    CompletableFuture<Boolean> addBonusTime(Query<Member<ObjectId>> query, int time);

    CompletableFuture<Boolean> setBonusTime(Query<Member<ObjectId>> query, int time);

    CompletableFuture<Boolean> setTotalTime(Query<Member<ObjectId>> query, int time);

    Query<Member<ObjectId>> asQuery(UUID userUUID);
}
