package com.visilabs.notificationbell;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.visilabs.notificationbell.model.NotificationBell;

/**
 * NotificationBell'i, host Activity'nin {@code android.R.id.content} view'ına
 * doğrudan ekler. Fragment ya da {@code FragmentActivity} gerektirmez; bu sayede
 * Flutter'ın {@code FlutterActivity}'si gibi {@code FragmentActivity} olmayan
 * host'larda da çalışır.
 *
 * <p>Ayrı bir (şeffaf) Activity açmak yerine mevcut Activity'nin içerik
 * hiyerarşisine eklenmesi önemlidir: bell kalıcı ve modal olmayan bir overlay
 * olduğu için, ayrı bir pencerede gösterildiğinde altındaki uygulamaya hiçbir
 * dokunuş ulaşamaz. Aynı pencerede ise bell'in şeffaf kök view'ı dokunuşları
 * tüketmez ve uygulama normal şekilde kullanılabilir kalır.
 */
public final class NotificationBellOverlay {

    private static final String LOG_TAG = "NotificationBell";

    private NotificationBellOverlay() {
    }

    public static void show(final Activity activity, final NotificationBell model) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            Log.e(LOG_TAG, "Activity is not available, cannot show the notification bell.");
            return;
        }
        if (model == null) {
            Log.e(LOG_TAG, "NotificationBell data is null, cannot show the notification bell.");
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ViewGroup content = activity.findViewById(android.R.id.content);
                    if (content == null) {
                        Log.e(LOG_TAG, "android.R.id.content is not available, cannot show the notification bell.");
                        return;
                    }

                    // Aynı anda birden fazla bell gösterilmesin. Her customEvent yeni bir
                    // aksiyon isteği tetiklediği için koruma olmadan bell'ler üst üste binerdi.
                    if (content.findViewWithTag(LOG_TAG) != null) {
                        Log.i(LOG_TAG, "NotificationBell already showing, skipping duplicate.");
                        return;
                    }

                    final Remover remover = new Remover(content);
                    NotificationBellController controller = new NotificationBellController(
                            activity, LayoutInflater.from(activity), content, model, remover);

                    View root = controller.getRoot();
                    root.setTag(LOG_TAG);
                    remover.view = root;

                    content.addView(root, new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    // Host Activity durduğunda overlay'i de kaldır (Fragment'ın
                    // onStop() -> endFragment() davranışının karşılığı).
                    registerLifecycleCleanup(activity, remover);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Could not show the notification bell overlay.", e);
                }
            }
        });
    }

    private static void registerLifecycleCleanup(final Activity activity, final Remover remover) {
        Application application = activity.getApplication();
        if (application == null) {
            return;
        }
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityStopped(@NonNull Activity stopped) {
                if (stopped != activity) {
                    return;
                }
                if (!activity.isChangingConfigurations()) {
                    remover.run();
                }
                Application app = activity.getApplication();
                if (app != null) {
                    app.unregisterActivityLifecycleCallbacks(this);
                }
            }

            @Override
            public void onActivityCreated(@NonNull Activity a, @Nullable Bundle b) {
            }

            @Override
            public void onActivityStarted(@NonNull Activity a) {
            }

            @Override
            public void onActivityResumed(@NonNull Activity a) {
            }

            @Override
            public void onActivityPaused(@NonNull Activity a) {
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity a, @NonNull Bundle b) {
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity a) {
            }
        });
    }

    private static final class Remover implements Runnable {
        private final ViewGroup content;
        private View view;

        Remover(ViewGroup content) {
            this.content = content;
        }

        @Override
        public void run() {
            if (view != null && view.getParent() == content) {
                content.removeView(view);
            }
        }
    }
}
