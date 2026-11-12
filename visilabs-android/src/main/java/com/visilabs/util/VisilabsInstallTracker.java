package com.visilabs.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.util.Log;

public class VisilabsInstallTracker {
    private static final String LOG_TAG = "VisilabsInstallTracker";
    private static final String PREF_NAME = "VisilabsInstallPreferences";
    private static final String KEY_INSTALLED = "Visilabs.installed";
    private static final String KEY_INSTALL_TYPE = "Visilabs.installType";
    private static final String KEY_FIRST_INSTALL_TIME = "Visilabs.firstInstallTime";

    public static final String TYPE_FIRST_INSTALL = "first_install";
    public static final String TYPE_REINSTALL = "reinstall";

    private static VisilabsInstallTracker sInstance;
    private final Context mContext;
    private String mInstallType = TYPE_FIRST_INSTALL;
    private final Object mLock = new Object();

    public static synchronized VisilabsInstallTracker getInstance(Context context) {
        if (sInstance == null && context != null) {
            sInstance = new VisilabsInstallTracker(context.getApplicationContext());
        }
        return sInstance;
    }

    public static VisilabsInstallTracker getInstance() {
        return sInstance;
    }

    private VisilabsInstallTracker(Context context) {
        mContext = context;
        evaluateInstallStatus();
    }

    private void evaluateInstallStatus() {
        synchronized (mLock) {
            SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            boolean hasInstalled = prefs.getBoolean(KEY_INSTALLED, false);

            if (hasInstalled) {
                mInstallType = prefs.getString(KEY_INSTALL_TYPE, TYPE_FIRST_INSTALL);
                return;
            }

            boolean isReinstall = false;
            try {
                PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                if (pInfo.firstInstallTime != pInfo.lastUpdateTime) {
                    isReinstall = true;
                }
            } catch (Exception e) {
                Log.w(LOG_TAG, "PackageInfo error: " + e.getMessage());
            }

            mInstallType = isReinstall ? TYPE_REINSTALL : TYPE_FIRST_INSTALL;
            prefs.edit()
                    .putBoolean(KEY_INSTALLED, true)
                    .putString(KEY_INSTALL_TYPE, mInstallType)
                    .putLong(KEY_FIRST_INSTALL_TIME, System.currentTimeMillis())
                    .apply();

            Log.i(LOG_TAG, "Install status evaluated: " + mInstallType);
        }
    }

    public boolean isFirstInstall() {
        synchronized (mLock) {
            return TYPE_FIRST_INSTALL.equals(mInstallType);
        }
    }

    public boolean isReinstall() {
        synchronized (mLock) {
            return TYPE_REINSTALL.equals(mInstallType);
        }
    }

    public String isFirstInstallValue(int tvc) {
        synchronized (mLock) {
            return (TYPE_FIRST_INSTALL.equals(mInstallType) && tvc <= 1) ? "1" : "0";
        }
    }

    public String isReinstallValue(int tvc) {
        synchronized (mLock) {
            return (TYPE_REINSTALL.equals(mInstallType) && tvc <= 1) ? "1" : "0";
        }
    }

    public String getInstallType() {
        synchronized (mLock) {
            return mInstallType;
        }
    }

    public void resetForTesting() {
        synchronized (mLock) {
            SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            prefs.edit().clear().apply();
            mInstallType = TYPE_FIRST_INSTALL;
        }
    }
}
