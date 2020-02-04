package com.visilabs.android;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

/**
 * Created by visilabs on 2.08.2016.
 */
public class VisilabsApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //Injector.INSTANCE.initApplicationComponent(this);
    }

}
