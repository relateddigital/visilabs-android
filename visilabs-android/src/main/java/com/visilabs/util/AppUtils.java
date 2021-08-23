package com.visilabs.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.visilabs.model.LocationPermission;

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

    public static LocationPermission getLocationPermissionStatus(Context context) {
        LocationPermission locationPermission = LocationPermission.NONE;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                locationPermission = LocationPermission.ALWAYS;
            } else {
                locationPermission = LocationPermission.NONE;
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationPermission = LocationPermission.ALWAYS;
            } else {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                    locationPermission = LocationPermission.APP_OPEN;
                } else {
                    locationPermission = LocationPermission.NONE;
                }
            }
        }
        return locationPermission;
    }
}
