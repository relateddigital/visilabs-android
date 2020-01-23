package com.visilabs.android;

import android.app.Application;

/**
 * Created by visilabs on 2.08.2016.
 */
public class VisilabsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Injector.INSTANCE.initApplicationComponent(this);

    }

}
