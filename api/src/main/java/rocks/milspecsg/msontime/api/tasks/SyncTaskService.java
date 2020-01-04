package rocks.milspecsg.msontime.api.tasks;

public interface SyncTaskService {

    void startSyncTask();

    void stopSyncTask();

    Runnable getSyncTask();
}
