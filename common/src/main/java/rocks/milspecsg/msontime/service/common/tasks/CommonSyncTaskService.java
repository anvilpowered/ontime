package rocks.milspecsg.msontime.service.common.tasks;

import rocks.milspecsg.msontime.api.tasks.SyncTaskService;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

public abstract class CommonSyncTaskService implements SyncTaskService {

    protected ConfigurationService configurationService;

    protected CommonSyncTaskService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
        this.configurationService.addConfigLoadedListener(this::loadConfig);
    }

    private void loadConfig(Object plugin) {
        stopSyncTask();
        startSyncTask();
    }
}
