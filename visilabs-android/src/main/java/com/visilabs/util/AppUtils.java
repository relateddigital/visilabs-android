package com.visilabs.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

public final class AppUtils {
    public static String appVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (Exception e) {
            Log.w("Visilabs", "Error when getting app version : " + e.toString());

        }
        return null;
    }
}
