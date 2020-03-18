package com.visilabs.gps.geofence;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.visilabs.Injector;
import com.visilabs.gps.manager.GpsManager;
import com.visilabs.util.VisilabsLog;


public class GeofenceMonitorConnection implements ServiceConnection {
    private final String LOG_TAG = "GeofenceMonitorConnection";

    public GeofenceMonitorConnection() {
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        //Cast Error Can be
        try{
            GeofenceMonitor geofenceMonitor = ((GeofenceMonitor.GeofenceMonitorBinder) service).getService();
            GpsManager gpsManager = Injector.INSTANCE.getGpsManager();
            if (gpsManager != null) {
                gpsManager.setGeoMonitorReference(geofenceMonitor);
            }
        }catch (Exception e){
            VisilabsLog.e(LOG_TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        GpsManager gpsManager = Injector.INSTANCE.getGpsManager();
        if (gpsManager != null) {
            gpsManager.setGeoMonitorReference(null);
        }
    }
}