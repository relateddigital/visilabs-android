package com.visilabs.android.gps.factory;

import android.content.Context;

import com.visilabs.android.gps.manager.GpsManager;

public class GpsFactory {
    private static final String TAG = "Visilabs GpsFactory";
    private static GpsManager gpsManager = null;

    public static GpsManager createManager(Context context) {
        if (gpsManager == null) {
            gpsManager = new GpsManager(context);
            return gpsManager;
        }
        return gpsManager;
    }
}
