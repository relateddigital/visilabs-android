package com.visilabs;


import com.visilabs.gps.manager.GpsManager;

public enum Injector {
    INSTANCE;

    private GpsManager gpsManager;

    Injector(){
    }

    public void initGpsManager(GpsManager gpsManager) {
        this.gpsManager = gpsManager;
    }

    public GpsManager getGpsManager() {
        return gpsManager;
    }
}