package com.visilabs.android;


import com.visilabs.android.gps.manager.GpsManager;
import com.visilabs.android.gps.manager.GpsManager2;

public enum Injector {
    INSTANCE;

    private GpsManager gpsManager;
    private GpsManager2 gpsManager2;
    //private GeofenceForegroundServiceHandler geofenceForegroundServiceHandler;


    private Injector(){
    }

    public void initGpsManager(GpsManager gpsManager) {
        this.gpsManager = gpsManager;
    }

    public void initGpsManager(GpsManager2 gpsManager2) {
        this.gpsManager2 = gpsManager2;
    }

    public GpsManager getGpsManager() {
        return gpsManager;
    }

    public GpsManager2 getGpsManager2() {
        return gpsManager2;
    }

}
