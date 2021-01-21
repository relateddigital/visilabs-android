package com.visilabs.gps.geofence;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class GeofenceMonitor extends VisilabsIntentService {

    private static final String TAG = "Visi GeoMonitor";
    private GpsManager mGpsManager;
    private final IBinder mBinder = new GeofenceMonitorBinder();
    private Context mContext;
    private GeofencingClient mGeofencingClient;
    private final List<VisilabsGeoFenceEntity> mPulGeoFenceEntitiesList = new ArrayList<>();
    private final LocationRequest mLocationRequest = new LocationRequest();
    private int mGpsPowerLevel;

    private TimerTask mTimerTask;
    private Timer mTimer;

    public class GeofenceMonitorBinder extends Binder {
        public GeofenceMonitor getService() {
            return GeofenceMonitor.this;
        }
    }

    public GeofenceMonitor() {
        super("GeofenceMonitor");
        Log.v(TAG, "Geofence Monitor initialized.");

        if(Visilabs.CallAPI() != null){
            mContext = Visilabs.CallAPI().getContext();
            if(Injector.INSTANCE.getGpsManager() == null){
                Injector.INSTANCE.initGpsManager(new GpsManager(mContext));
            }
            mGpsManager = Injector.INSTANCE.getGpsManager();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return mBinder;
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
                mContext = getApplicationContext();
                Visilabs.CreateAPI(mContext);

                if (Injector.INSTANCE.getGpsManager() == null) {
                    Injector.INSTANCE.initGpsManager(new GpsManager(mContext));
                }
                mGpsManager = Injector.INSTANCE.getGpsManager();
            }

            mGeofencingClient = LocationServices.getGeofencingClient(mContext);
        }catch (Exception e){
            VisilabsLog.e(TAG, e.getMessage(), e);
        }
    }

    private void cleanupService() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    public void removeGeofences(final List<VisilabsGeoFenceEntity> geoFencesToRemove) {
        if (mGeofencingClient != null) {
            final List<String> IdsToRemove = new ArrayList<>();
            for (VisilabsGeoFenceEntity geofenceEntity : geoFencesToRemove) {
                IdsToRemove.add(geofenceEntity.getGuid());
            }

            if (IdsToRemove.isEmpty())
                return;

            new Thread(new Runnable() {
                public void run() {
                    try{
                        mGeofencingClient.removeGeofences(IdsToRemove);
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
        if (mGeofencingClient!=null) {
            final List<Geofence> geofences = new ArrayList<>();
            for (VisilabsGeoFenceEntity geoFenceEntity : geoFencesToAdd) {
                Geofence newGf = geoFenceEntity.toGeofence();
                geofences.add(newGf);
            }

            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                final PendingIntent geoFenceIntent = GeoFencesUtils.getTransitionPendingIntent(mContext);

                mGeofencingClient.addGeofences(getAddGeofencingRequest(geofences), geoFenceIntent)
                        .addOnSuccessListener((Activity) mContext, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Registering geofence : ", "Successful");
                            }
                        })
                        .addOnFailureListener((Activity) mContext, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Registering geofence : ", "Failed -> " + e.getMessage());
                            }
                        });
            }


        } else {
            mPulGeoFenceEntitiesList.addAll(geoFencesToAdd);
        }
    }

    @SuppressLint("MissingPermission")
    public void setMediumPowerGPS() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return;
        if (mGeofencingClient == null)
            return;
        if (mGpsPowerLevel == 2)
            return;
        mGpsPowerLevel = 2;

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Log.v(TAG, "onLocationChanged");
                mGpsManager.setLastKnownLocation(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(mContext).removeLocationUpdates(locationCallback);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        LocationServices.getFusedLocationProviderClient(mContext).requestLocationUpdates(mLocationRequest,
                locationCallback, Looper.getMainLooper());
    }
}