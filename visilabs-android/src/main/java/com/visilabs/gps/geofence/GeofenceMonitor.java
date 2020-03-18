package com.visilabs.gps.geofence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;


import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.visilabs.Visilabs;
import com.visilabs.Injector;
import com.visilabs.gps.entities.VisilabsGeoFenceEntity;
import com.visilabs.gps.manager.GpsManager;
import com.visilabs.util.VisilabsIntentService;
import com.visilabs.gps.util.GeoFencesUtils;
import com.visilabs.util.VisilabsLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GeofenceMonitor extends VisilabsIntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "Visi GeoMonitor";
    private GpsManager gpsManager;
    private final IBinder binder = new GeofenceMonitorBinder();
    private Context context;
    private GoogleApiClient googleApiClient;
    private final List<VisilabsGeoFenceEntity> pulGeoFenceEntitiesList = new ArrayList<>();
    private final LocationRequest mLocationRequest = new LocationRequest();
    private int GPS_POWER_LVL;

    private TimerTask timerTask;
    private Timer timer;

    public class GeofenceMonitorBinder extends Binder {
        public GeofenceMonitor getService() {
            return GeofenceMonitor.this;
        }
    }

    public GeofenceMonitor() {
        super("GeofenceMonitor");
        Log.v(TAG, "Geofence Monitor initialized.");

        if(Visilabs.CallAPI() != null){
            this.context = Visilabs.CallAPI().getContext();
            if(Injector.INSTANCE.getGpsManager() == null){
                Injector.INSTANCE.initGpsManager(new GpsManager(this.context));
            }
            this.gpsManager = Injector.INSTANCE.getGpsManager();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return binder;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        connectGoogleApi();
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        cleanupService();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e(TAG, "onTaskRemoved");
        cleanupService();
    }

    private void connectGoogleApi() {
        try {

            if (Visilabs.CallAPI() == null) {
                this.context = getApplicationContext();
                Visilabs.CreateAPI(this.context);

                if (Injector.INSTANCE.getGpsManager() == null) {
                    Injector.INSTANCE.initGpsManager(new GpsManager(this.context));
                }
                this.gpsManager = Injector.INSTANCE.getGpsManager();
            }

            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            googleApiClient.connect();
        }catch (Exception e){
            VisilabsLog.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        setMediumPowerGPS();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    private void cleanupService() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void removeGeofences(final List<VisilabsGeoFenceEntity> geoFencesToRemove) {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            final List<String> IdsToRemove = new ArrayList<>();
            for (VisilabsGeoFenceEntity geofenceEntity : geoFencesToRemove) {
                IdsToRemove.add(geofenceEntity.guid);
            }

            if (IdsToRemove.isEmpty())
                return;

            new Thread(new Runnable() {
                public void run() {
                    try{
                        LocationServices.GeofencingApi.removeGeofences(googleApiClient, IdsToRemove).await();
                    }catch (Exception ex){
                        Log.e(TAG, ex.getMessage(), ex);
                    }
                }
            }).start();
        }
    }

    private GeofencingRequest getAddGeofencingRequest(List<Geofence> geofences) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.addGeofences(geofences);
        return builder.build();
    }

    public void addGeofences(final List<VisilabsGeoFenceEntity> geoFencesToAdd) {
        if (googleApiClient.isConnected()) {
            final List<Geofence> geofences = new ArrayList<>();
            for (VisilabsGeoFenceEntity geoFenceEntity : geoFencesToAdd) {
                Geofence newGf = geoFenceEntity.toGeofence();
                geofences.add(newGf);
            }

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                final PendingIntent geoFenceIntent = GeoFencesUtils.getTransitionPendingIntent(context);
                PendingResult<Status> result = LocationServices.GeofencingApi.addGeofences(googleApiClient, getAddGeofencingRequest(geofences), geoFenceIntent);
                result.setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.d("Registering geofence", String.valueOf(true));
                        } else {
                            Log.e(TAG, "Registering geofence failed: " + status.getStatusMessage() + " : " + status.getStatusCode());
                        }
                    }
                });
            }


        } else {
            pulGeoFenceEntitiesList.addAll(geoFencesToAdd);
        }
    }

    public void setMediumPowerGPS() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return;
        if (googleApiClient == null || !googleApiClient.isConnected())
            return;
        if (GPS_POWER_LVL == 2)
            return;
        GPS_POWER_LVL = 2;
        if (Looper.myLooper() == null)
            Looper.prepare();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, this);
        if (Looper.myLooper() == null)
            Looper.loop();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "onLocationChanged");
        gpsManager.setLastKnownLocation(location);
    }
}