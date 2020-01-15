package rocks.milspecsg.msontime.service.common.data.registry;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import rocks.milspecsg.msrepository.api.data.key.Keys;
import rocks.milspecsg.msrepository.common.data.registry.CommonExtendedRegistry;

@Singleton
public class MSOnTimeRegistry extends CommonExtendedRegistry {

    @Inject
    public MSOnTimeRegistry() {
        defaultMap.put(Keys.BASE_SCAN_PACKAGE, "rocks.milspecsg.msontime.model.core");
    }
}
