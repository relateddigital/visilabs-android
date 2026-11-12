package androidx.core.app;

import android.util.Log;

/**
 * {@link JobIntentService} that does not crash when JobScheduler has already stopped the job
 * while the next work item is dequeued.
 */
public abstract class VisilabsSafeJobIntentService extends JobIntentService {

    private static final String LOG_TAG = "VisilabsSafeJIS";

    @Override
    GenericWorkItem dequeueWork() {
        try {
            return super.dequeueWork();
        } catch (SecurityException e) {
            Log.w(LOG_TAG, "Could not dequeue the work!", e);
            return null;
        }
    }
}
