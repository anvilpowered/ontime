package rocks.milspecsg.msontime;

import com.google.common.reflect.TypeToken;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import rocks.milspecsg.msontime.api.config.ConfigKeys;
import rocks.milspecsg.msontime.api.member.MemberManager;
import rocks.milspecsg.msontime.api.member.repository.MemberRepository;
import rocks.milspecsg.msontime.service.common.config.MSOnTimeConfigurationService;
import rocks.milspecsg.msontime.service.common.member.CommonMemberManager;
import rocks.milspecsg.msontime.service.common.member.repository.CommonMongoMemberRepository;
import rocks.milspecsg.msrepository.BindingExtensions;
import rocks.milspecsg.msrepository.CommonBindingExtensions;
import rocks.milspecsg.msrepository.PluginInfo;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;
import rocks.milspecsg.msrepository.api.manager.annotation.MongoDBComponent;
import rocks.milspecsg.msrepository.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.datastore.mongodb.MongoConfig;
import rocks.milspecsg.msrepository.datastore.mongodb.MongoContext;

@SuppressWarnings("UnstableApiUsage")
public class CommonModule<
    TUser,
    TString,
    TCommandSource>
    extends AbstractModule {

    private static final String BASE_SCAN_PACKAGE = "rocks.milspecsg.msontime.model.core";

    @Override
    protected void configure() {

        BindingExtensions be = new CommonBindingExtensions(binder());

        bind(MongoConfig.class).toInstance(
            new MongoConfig(
                BASE_SCAN_PACKAGE,
                ConfigKeys.DATA_STORE_NAME,
                ConfigKeys.MONGODB_HOSTNAME,
                ConfigKeys.MONGODB_PORT,
                ConfigKeys.MONGODB_DBNAME,
                ConfigKeys.MONGODB_USERNAME,
                ConfigKeys.MONGODB_PASSWORD,
                ConfigKeys.MONGODB_USE_AUTH
            )
        );

        be.bind(
            new TypeToken<MemberRepository<?, ?, ?>>(getClass()) {
            },
            new TypeToken<MemberRepository<ObjectId, Datastore, MongoConfig>>(getClass()) {
            },
            new TypeToken<CommonMongoMemberRepository>(getClass()) {
            },
            MongoDBComponent.class
        );

        be.bind(
            new TypeToken<MemberManager<TString>>(getClass()) {
            },
            new TypeToken<CommonMemberManager<TUser, TString, TCommandSource>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<PluginInfo<TString>>(getClass()) {
            },
            new TypeToken<MSOnTimePluginInfo<TString, TCommandSource>>(getClass()) {
            }
        );

        bind(new TypeLiteral<DataStoreContext<ObjectId, Datastore, MongoConfig>>() {
        }).to(new TypeLiteral<MongoContext>() {
        });

        bind(ConfigurationService.class).to(MSOnTimeConfigurationService.class);
    }
}
