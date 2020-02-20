package com.visilabs.android.gps.listener;

import com.google.android.gms.location.Geofence;

public interface IVisilabsGeofenceListener {
    void onEnter(Geofence geofence);

    void onExit(Geofence geofence);

    void onTrigger(Geofence geofence);
}