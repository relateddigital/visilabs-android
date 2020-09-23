package com.visilabs;


import com.visilabs.gps.manager.GpsManager;
import com.visilabs.gps.manager.GpsManagerMoreThanOreo;

public enum Injector {
    INSTANCE;

    private GpsManager gpsManager;
    private GpsManagerMoreThanOreo gpsManagerMoreThanOreo;
    //private GeofenceForegroundServiceHandler geofenceForegroundServiceHandler;

    Injector(){
    }

    public void initGpsManager(GpsManager gpsManager) {
        this.gpsManager = gpsManager;
    }

    public void initGpsManager(GpsManagerMoreThanOreo gpsManagerMoreThanOreo) {
        this.gpsManagerMoreThanOreo = gpsManagerMoreThanOreo;
    }

    public GpsManager getGpsManager() {
        return gpsManager;
    }

    public GpsManagerMoreThanOreo getGpsManagerMoreThanOreo() {
        return gpsManagerMoreThanOreo;
    }
}