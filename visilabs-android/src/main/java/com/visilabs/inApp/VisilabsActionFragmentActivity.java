package com.visilabs.inApp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.visilabs.countdownTimerBanner.CountdownTimerBannerFragment;
import com.visilabs.countdownTimerBanner.model.CountdownTimerBanner;
import com.visilabs.inApp.customactions.CustomActionFragment;
import com.visilabs.inApp.customactions.model.CustomActions;
import com.visilabs.inappnotification.DrawerModel;
import com.visilabs.inappnotification.InAppNotificationFragment;
import com.visilabs.notificationbell.NotificationBellFragment;
import com.visilabs.notificationbell.model.NotificationBell;

import java.io.Serializable;

/**
 * Şeffaf host activity. Uygulamanın ana Activity'si bir {@link FragmentActivity}
 * olmadığında (ör. Flutter'ın {@code FlutterActivity}'si) fragment tabanlı
 * aksiyonların (custom action, social proof, drawer, notification bell,
 * countdown timer banner) yine de gösterilebilmesini sağlar.
 */
public class VisilabsActionFragmentActivity extends FragmentActivity {

    private static final String LOG_TAG = "VisilabsActionFragment";

    public static final String EXTRA_TYPE = "action-fragment-type";
    public static final String EXTRA_DATA = "action-fragment-data";

    public static final String TYPE_CUSTOM_ACTION = "custom_action";
    public static final String TYPE_SOCIAL_PROOF = "social_proof";
    public static final String TYPE_DRAWER = "drawer";
    public static final String TYPE_NOTIFICATION_BELL = "notification_bell";
    public static final String TYPE_COUNTDOWN_TIMER_BANNER = "countdown_timer_banner";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fragment kaldırıldığında (dismiss) host activity'yi de kapat.
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(
                new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
                        super.onFragmentDetached(fm, f);
                        if (!isFinishing()) {
                            finish();
                        }
                    }
                }, false);

        // Konfigürasyon değişiminde FragmentManager fragment'ı kendisi geri yükler.
        if (savedInstanceState != null) {
            return;
        }

        String type = getIntent() != null ? getIntent().getStringExtra(EXTRA_TYPE) : null;
        Serializable data = getIntent() != null ? getIntent().getSerializableExtra(EXTRA_DATA) : null;

        if (type == null || data == null) {
            Log.e(LOG_TAG, "Missing action type or data, closing host activity.");
            finish();
            return;
        }

        Fragment fragment = createFragment(type, data);
        if (fragment == null) {
            Log.e(LOG_TAG, "Could not create fragment for type: " + type);
            finish();
            return;
        }

        try {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, fragment)
                    .commit();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not add fragment for type: " + type, e);
            finish();
        }
    }

    @Nullable
    private Fragment createFragment(@NonNull String type, @NonNull Serializable data) {
        try {
            switch (type) {
                case TYPE_CUSTOM_ACTION:
                    return CustomActionFragment.newInstance((CustomActions) data);
                case TYPE_SOCIAL_PROOF:
                    return SocialProofFragment.newInstance((ProductStatNotifierModel) data);
                case TYPE_DRAWER:
                    return InAppNotificationFragment.newInstance((DrawerModel) data);
                case TYPE_NOTIFICATION_BELL:
                    return NotificationBellFragment.newInstance((NotificationBell) data);
                case TYPE_COUNTDOWN_TIMER_BANNER:
                    return CountdownTimerBannerFragment.newInstance((CountdownTimerBanner) data);
                default:
                    return null;
            }
        } catch (ClassCastException e) {
            Log.e(LOG_TAG, "Action data does not match the type: " + type, e);
            return null;
        }
    }
}
