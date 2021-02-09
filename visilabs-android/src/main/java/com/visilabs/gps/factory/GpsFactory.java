package com.visilabs.gps.factory;

import android.content.Context;

import com.visilabs.gps.manager.GpsManager;

/**
 * Created by visilabs on 13.07.2016.
 */
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