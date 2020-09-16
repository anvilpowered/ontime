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

package org.anvilpowered.ontime.common.member;

import com.google.inject.Inject;
import jetbrains.exodus.entitystore.Entity;
import jetbrains.exodus.entitystore.EntityId;
import jetbrains.exodus.entitystore.PersistentEntityStore;
import jetbrains.exodus.entitystore.StoreTransaction;
import org.anvilpowered.anvil.api.datastore.DataStoreContext;
import org.anvilpowered.anvil.base.datastore.BaseXodusRepository;
import org.anvilpowered.ontime.api.member.XodusMemberRepository;
import org.anvilpowered.ontime.api.model.member.Member;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CommonXodusMemberRepository
    extends CommonMemberRepository<EntityId, PersistentEntityStore>
    implements BaseXodusRepository<Member<EntityId>>,
    XodusMemberRepository {

    @Inject
    public CommonXodusMemberRepository(DataStoreContext<EntityId, PersistentEntityStore> dataStoreContext) {
        super(dataStoreContext);
    }

    @Override
    public CompletableFuture<Optional<Member<EntityId>>> getOneForUser(UUID userUUID) {
        return getOne(asQuery(userUUID));
    }

    @Override
    public CompletableFuture<Boolean> addTimeForUser(UUID userUUID, long time) {
        return addTime(asQuery(userUUID), time);
    }

    @Override
    public CompletableFuture<Boolean> addBonusTimeForUser(UUID userUUID, long time) {
        return addBonusTime(asQuery(userUUID), time);
    }

    @Override
    public CompletableFuture<Boolean> setBonusTimeForUser(UUID userUUID, long time) {
        return setBonusTime(asQuery(userUUID), time);
    }

    @Override
    public CompletableFuture<Boolean> setTotalTimeForUser(UUID userUUID, long time) {
        return setTotalTime(asQuery(userUUID), time);
    }

    @Override
    public Function<? super StoreTransaction, ? extends Iterable<Entity>> asQuery(UUID userUUID) {
        return txn -> txn.find(getTClass().getSimpleName(), "userUUID", userUUID.toString());
    }

    @Override
    public CompletableFuture<Boolean> addTime(
        Function<? super StoreTransaction, ? extends Iterable<Entity>> query, long time) {
        return update(query, e -> {
            Comparable<?> playTime = e.getProperty("playTime");
            if (playTime instanceof Long) {
                e.setProperty("playTime", ((Long) playTime) + time);
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> addBonusTime(
        Function<? super StoreTransaction, ? extends Iterable<Entity>> query, long time) {
        return update(query, e -> {
            Comparable<?> bonusTime = e.getProperty("bonusTime");
            if (bonusTime instanceof Long) {
                e.setProperty("bonusTime", (Long) bonusTime + time);
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> setBonusTime(
        Function<? super StoreTransaction, ? extends Iterable<Entity>> query, long time) {
        return update(query, e -> e.setProperty("bonusTime", time));
    }

    @Override
    public CompletableFuture<Boolean> setTotalTime(
        Function<? super StoreTransaction, ? extends Iterable<Entity>> query, long time) {
        return update(query, e -> {
            Comparable<?> playTime = e.getProperty("playTime");
            if (playTime instanceof Long) {
                e.setProperty("bonusTime", time - (Long) playTime);
            }
        });
    }
}
