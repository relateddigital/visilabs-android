package com.visilabs.gps.factory;

import android.content.Context;

import com.visilabs.gps.manager.GpsManagerMoreThanOreo;

/**
 * Created by visilabs on 13.07.2016.
 */
public class GpsFactory2 {
    private static final String TAG = "Visilabs GpsFactory2";
    private static GpsManagerMoreThanOreo gpsManager = null;

    public static GpsManagerMoreThanOreo createManager(Context context) {
        if (gpsManager == null) {
            gpsManager = new GpsManagerMoreThanOreo(context);
            return gpsManager;
        }
        return gpsManager;
    }
}