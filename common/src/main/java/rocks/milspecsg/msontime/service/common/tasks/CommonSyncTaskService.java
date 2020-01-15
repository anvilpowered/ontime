package rocks.milspecsg.msontime.service.common.tasks;

import rocks.milspecsg.msontime.api.tasks.SyncTaskService;
import rocks.milspecsg.msrepository.api.data.registry.Registry;

public abstract class CommonSyncTaskService implements SyncTaskService {

    protected Registry registry;

    protected CommonSyncTaskService(Registry registry) {
        this.registry = registry;
        this.registry.addRegistryLoadedListener(this::registryLoaded);
    }

    private void registryLoaded(Object plugin) {
        stopSyncTask();
        startSyncTask();
    }
}
