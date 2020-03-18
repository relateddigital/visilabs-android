package com.visilabs.gps.factory;

import android.content.Context;

import com.visilabs.gps.manager.GpsManager2;

/**
 * Created by visilabs on 13.07.2016.
 */
public class GpsFactory2 {
    private static final String TAG = "Visilabs GpsFactory2";
    private static GpsManager2 gpsManager = null;

    public static GpsManager2 createManager(Context context) {
        if (gpsManager == null) {
            gpsManager = new GpsManager2(context);
            return gpsManager;
        }
        return gpsManager;
    }
}