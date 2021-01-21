package com.visilabs.gps.factory;

import android.content.Context;

import com.visilabs.gps.manager.GpsManagerMoreThanOreo;

/**
 * Created by visilabs on 13.07.2016.
 */
public class GpsFactory2 {
    private static GpsManagerMoreThanOreo mGpsManager = null;

    public static GpsManagerMoreThanOreo createManager(Context context) {
        if (mGpsManager == null) {
            mGpsManager = new GpsManagerMoreThanOreo(context);
            return mGpsManager;
        }
        return mGpsManager;
    }
}