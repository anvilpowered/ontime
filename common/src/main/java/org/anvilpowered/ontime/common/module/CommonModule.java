/*
 *   OnTime - AnvilPowered
 *   Copyright (C) 2020 Cableguy20
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

package org.anvilpowered.ontime.common.module;

import com.google.common.reflect.TypeToken;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.anvilpowered.anvil.api.Anvil;
import org.anvilpowered.anvil.api.data.config.ConfigurationService;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.datastore.DataStoreContext;
import org.anvilpowered.anvil.api.datastore.MongoContext;
import org.anvilpowered.anvil.api.manager.annotation.MongoDBComponent;
import org.anvilpowered.anvil.api.misc.BindingExtensions;
import org.anvilpowered.anvil.api.plugin.BasicPluginInfo;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.ontime.api.member.MemberManager;
import org.anvilpowered.ontime.api.member.repository.MemberRepository;
import org.anvilpowered.ontime.api.util.DataImportService;
import org.anvilpowered.ontime.common.data.config.OnTimeConfigurationService;
import org.anvilpowered.ontime.common.data.registry.OnTimeRegistry;
import org.anvilpowered.ontime.common.member.CommonMemberManager;
import org.anvilpowered.ontime.common.member.repository.CommonMongoMemberRepository;
import org.anvilpowered.ontime.common.plugin.OnTimePluginInfo;
import org.anvilpowered.ontime.common.util.CommonDataImportService;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

@SuppressWarnings("UnstableApiUsage")
public class CommonModule<
    TUser,
    TPlayer extends TCommandSource,
    TString,
    TCommandSource>
    extends AbstractModule {

    @Override
    protected void configure() {

        BindingExtensions be = Anvil.getBindingExtensions(binder());

        be.bind(
            new TypeToken<MemberRepository<?, ?>>(getClass()) {
            },
            new TypeToken<MemberRepository<ObjectId, Datastore>>(getClass()) {
            },
            new TypeToken<CommonMongoMemberRepository>(getClass()) {
            },
            MongoDBComponent.class
        );

        be.bind(
            new TypeToken<MemberManager<TString>>(getClass()) {
            },
            new TypeToken<CommonMemberManager<TUser, TPlayer, TString, TCommandSource>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<BasicPluginInfo>(getClass()) {
            },
            new TypeToken<OnTimePluginInfo<TString, TCommandSource>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<PluginInfo<TString>>(getClass()) {
            },
            new TypeToken<OnTimePluginInfo<TString, TCommandSource>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<DataImportService>(getClass()) {
            },
            new TypeToken<CommonDataImportService<TString>>(getClass()) {
            }
        );

        bind(new TypeLiteral<DataStoreContext<ObjectId, Datastore>>() {
        }).to(MongoContext.class);

        bind(ConfigurationService.class).to(OnTimeConfigurationService.class);
        bind(Registry.class).to(OnTimeRegistry.class);
    }
}
