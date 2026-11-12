package com.visilabs.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class VisilabsInAppActionManager {
    private static final String LOG_TAG = "VisilabsActionManager";

    // Deduplication window: 30 seconds
    private static final long DEDUPLICATION_INTERVAL_MILLIS = 30000L;
    // Chained dismiss threshold: 5 seconds
    private static final long CHAINED_DISMISS_THRESHOLD_MILLIS = 5000L;
    // Fast track transition delay: 300 ms (0.3s)
    private static final long FAST_TRACK_TRANSITION_DELAY_MILLIS = 300L;

    private static final Handler sMainHandler = new Handler(Looper.getMainLooper());
    private static final Map<String, Long> sRecentlyShownActionIds = Collections.synchronizedMap(new HashMap<String, Long>());

    private static volatile long sLastDismissedTime = 0L;
    private static volatile boolean sIsActionShowing = false;

    private static ScheduledActionItem sScheduledAction = null;
    private static final List<PendingActionItem> sPendingQueue = new ArrayList<>();
    private static boolean sIsLifecycleRegistered = false;

    private static final class ScheduledActionItem {
        final Object actId;
        final Runnable runnable;
        final long scheduledAt;
        final long originalDelay;

        ScheduledActionItem(Object actId, Runnable runnable, long scheduledAt, long originalDelay) {
            this.actId = actId;
            this.runnable = runnable;
            this.scheduledAt = scheduledAt;
            this.originalDelay = originalDelay;
        }
    }

    private static final class PendingActionItem {
        final Object actId;
        final Runnable action;

        PendingActionItem(Object actId, Runnable action) {
            this.actId = actId;
            this.action = action;
        }
    }

    private VisilabsInAppActionManager() {}

    public static synchronized void init(Application application) {
        if (sIsLifecycleRegistered || application == null) return;
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
            @Override
            public void onActivityStarted(Activity activity) {}
            @Override
            public void onActivityResumed(Activity activity) {
                if (isSdkActivity(activity)) {
                    sIsActionShowing = true;
                } else {
                    ActivityUtils.setParentActivity(activity);
                }
            }
            @Override
            public void onActivityPaused(Activity activity) {}
            @Override
            public void onActivityStopped(Activity activity) {}
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
            @Override
            public void onActivityDestroyed(Activity activity) {
                if (isSdkActivity(activity)) {
                    onActionDismissed();
                } else if (ActivityUtils.getParentActivity() == activity) {
                    ActivityUtils.setParentActivity(null);
                }
            }
        });
        sIsLifecycleRegistered = true;
        Log.i(LOG_TAG, "VisilabsInAppActionManager initialized with ActivityLifecycleCallbacks");
    }

    private static boolean isSdkActivity(Activity activity) {
        if (activity == null) return false;
        String name = activity.getClass().getName();
        return (name.startsWith("com.visilabs.inApp.") ||
                name.startsWith("com.visilabs.survey.") ||
                name.startsWith("com.visilabs.spinToWin.") ||
                name.startsWith("com.visilabs.scratchToWin.") ||
                name.startsWith("com.visilabs.mailSub.") ||
                name.startsWith("com.visilabs.story.")) &&
                !name.contains("PermissionActivity");
    }

    public static long getEffectiveDelay(long delayMillis) {
        long timeSinceDismiss = System.currentTimeMillis() - sLastDismissedTime;
        if (sLastDismissedTime > 0 && timeSinceDismiss < CHAINED_DISMISS_THRESHOLD_MILLIS) {
            Log.i(LOG_TAG, "Chained action detected (dismissed " + timeSinceDismiss + "ms ago). Overriding delay " + delayMillis + "ms to " + FAST_TRACK_TRANSITION_DELAY_MILLIS + "ms.");
            return FAST_TRACK_TRANSITION_DELAY_MILLIS;
        }
        return delayMillis;
    }

    public static boolean isDuplicate(Object actId) {
        if (actId == null) return false;
        String idStr = String.valueOf(actId);
        if (idStr.isEmpty() || "0".equals(idStr) || "null".equals(idStr)) return false;
        Long lastShown = sRecentlyShownActionIds.get(idStr);
        if (lastShown != null) {
            long elapsed = System.currentTimeMillis() - lastShown;
            if (elapsed < DEDUPLICATION_INTERVAL_MILLIS) {
                Log.i(LOG_TAG, "Action with actId " + idStr + " was shown " + elapsed + "ms ago, deduplicating.");
                return true;
            }
        }
        return false;
    }

    public static void markShown(Object actId) {
        if (actId != null) {
            String idStr = String.valueOf(actId);
            if (!idStr.isEmpty() && !"0".equals(idStr) && !"null".equals(idStr)) {
                sRecentlyShownActionIds.put(idStr, System.currentTimeMillis());
            }
        }
    }

    public static synchronized boolean scheduleOrShow(final Object actId, long rawDelayMillis, final Runnable action) {
        if (isDuplicate(actId)) {
            return false;
        }

        markShown(actId);

        long effectiveDelay = getEffectiveDelay(rawDelayMillis);

        if (sIsActionShowing) {
            Log.i(LOG_TAG, "An action is currently showing. Queueing actId=" + actId + " for display on dismissal.");
            sPendingQueue.add(new PendingActionItem(actId, action));
            return true;
        }

        if (effectiveDelay > 0) {
            if (sScheduledAction != null) {
                sMainHandler.removeCallbacks(sScheduledAction.runnable);
            }
            final Runnable[] itemRunnableHolder = new Runnable[1];
            itemRunnableHolder[0] = new Runnable() {
                @Override
                public void run() {
                    synchronized (VisilabsInAppActionManager.class) {
                        sScheduledAction = null;
                        sIsActionShowing = true;
                    }
                    action.run();
                }
            };
            ScheduledActionItem item = new ScheduledActionItem(
                    actId,
                    itemRunnableHolder[0],
                    System.currentTimeMillis(),
                    effectiveDelay
            );
            sScheduledAction = item;
            sMainHandler.postDelayed(item.runnable, effectiveDelay);
            Log.i(LOG_TAG, "Scheduled action actId=" + actId + " with effective delay " + effectiveDelay + "ms");
        } else {
            sIsActionShowing = true;
            sMainHandler.post(action);
        }

        return true;
    }

    public static synchronized void onActionDismissed() {
        sLastDismissedTime = System.currentTimeMillis();
        sIsActionShowing = false;
        Log.i(LOG_TAG, "Action dismissed at " + sLastDismissedTime + ". Checking scheduled or pending actions...");

        if (sScheduledAction != null) {
            final ScheduledActionItem currentScheduled = sScheduledAction;
            sMainHandler.removeCallbacks(currentScheduled.runnable);
            sScheduledAction = null;
            Log.i(LOG_TAG, "Fast-tracking scheduled action (actId=" + currentScheduled.actId + ") on dismissal with " + FAST_TRACK_TRANSITION_DELAY_MILLIS + "ms delay.");
            sMainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    synchronized (VisilabsInAppActionManager.class) {
                        sIsActionShowing = true;
                    }
                    currentScheduled.runnable.run();
                }
            }, FAST_TRACK_TRANSITION_DELAY_MILLIS);
            return;
        }

        if (!sPendingQueue.isEmpty()) {
            final PendingActionItem nextItem = sPendingQueue.remove(0);
            Log.i(LOG_TAG, "Displaying next pending action from queue (actId=" + nextItem.actId + ") with " + FAST_TRACK_TRANSITION_DELAY_MILLIS + "ms delay.");
            sMainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    synchronized (VisilabsInAppActionManager.class) {
                        sIsActionShowing = true;
                    }
                    nextItem.action.run();
                }
            }, FAST_TRACK_TRANSITION_DELAY_MILLIS);
        }
    }
}
