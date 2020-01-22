package rocks.milspecsg.msontime;

import com.google.common.reflect.TypeToken;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import rocks.milspecsg.msontime.api.member.MemberManager;
import rocks.milspecsg.msontime.api.member.repository.MemberRepository;
import rocks.milspecsg.msontime.api.plugin.PluginMessages;
import rocks.milspecsg.msontime.api.util.DataImportService;
import rocks.milspecsg.msontime.plugin.MSOnTimePluginInfo;
import rocks.milspecsg.msontime.service.common.data.config.MSOnTimeConfigurationService;
import rocks.milspecsg.msontime.service.common.data.registry.MSOnTimeRegistry;
import rocks.milspecsg.msontime.service.common.member.CommonMemberManager;
import rocks.milspecsg.msontime.service.common.member.repository.CommonMongoMemberRepository;
import rocks.milspecsg.msontime.service.common.util.CommonDataImportService;
import rocks.milspecsg.msontime.plugin.CommonPluginMessages;
import rocks.milspecsg.msrepository.api.data.config.ConfigurationService;
import rocks.milspecsg.msrepository.api.data.registry.Registry;
import rocks.milspecsg.msrepository.api.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.api.datastore.MongoContext;
import rocks.milspecsg.msrepository.api.manager.annotation.MongoDBComponent;
import rocks.milspecsg.msrepository.api.misc.BindingExtensions;
import rocks.milspecsg.msrepository.api.util.PluginInfo;
import rocks.milspecsg.msrepository.common.misc.CommonBindingExtensions;

@SuppressWarnings("UnstableApiUsage")
public class CommonModule<
    TUser,
    TPlayer extends TCommandSource,
    TString,
    TCommandSource>
    extends AbstractModule {

    @Override
    protected void configure() {

        BindingExtensions be = new CommonBindingExtensions(binder());

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
            new TypeToken<PluginInfo<TString>>(getClass()) {
            },
            new TypeToken<MSOnTimePluginInfo<TString, TCommandSource>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<DataImportService>(getClass()) {
            },
            new TypeToken<CommonDataImportService<TString>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<PluginMessages<TString>>(getClass()) {
            },
            new TypeToken<CommonPluginMessages<TString, TCommandSource>>(getClass()) {
            }
        );

        bind(new TypeLiteral<DataStoreContext<ObjectId, Datastore>>() {
        }).to(new TypeLiteral<MongoContext>() {
        });

        bind(ConfigurationService.class).to(MSOnTimeConfigurationService.class);
        bind(Registry.class).to(MSOnTimeRegistry.class);
    }
}
