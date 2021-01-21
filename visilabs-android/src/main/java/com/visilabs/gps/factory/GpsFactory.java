package com.visilabs.gps.factory;

import android.content.Context;

import com.visilabs.gps.manager.GpsManager;

public class GpsFactory {
    private static GpsManager mGpsManager = null;

    public static GpsManager createManager(Context context) {
        if (mGpsManager == null) {
            mGpsManager = new GpsManager(context);
            return mGpsManager;
        }
        return mGpsManager;
    }
}
