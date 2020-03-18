package com.visilabs.gps.factory;

import android.content.Context;

import com.visilabs.gps.manager.GpsManager;

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
