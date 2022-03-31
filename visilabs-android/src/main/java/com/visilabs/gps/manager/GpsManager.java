package com.visilabs.gps.manager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.visilabs.Injector;
import com.visilabs.api.VisilabsGeofenceGetListCallback;
import com.visilabs.api.VisilabsGeofenceRequest;
import com.visilabs.gps.entities.VisilabsGeoFenceEntity;
import com.visilabs.gps.geofence.GeofenceBroadcastReceiver;
import com.visilabs.gps.geofence.VisilabsAlarm;
import com.visilabs.gps.model.VisilabsGeofenceGetListResponse;
import com.visilabs.gps.util.GeoFencesUtils;
import com.visilabs.util.VisilabsConstant;
import com.visilabs.util.VisilabsLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class GpsManager {

    private final String TAG = "Visilabs GpsManager2";
    public final List<VisilabsGeoFenceEntity> mActiveGeoFenceEntityList = new ArrayList<>();
    private final List<VisilabsGeoFenceEntity> mAllGeoFenceEntityList = new ArrayList<>();
    private final List<VisilabsGeoFenceEntity> mToAddGeoFenceEntityList = new ArrayList<>();
    private final List<VisilabsGeoFenceEntity> mToRemoveGeoFenceEntityList = new ArrayList<>();
    private final Context mApplication;
    public boolean mIsManagerActive = false;
    public boolean mIsManagerStarting = false;
    private Location mLastKnownLocation = null;
    private boolean mFirstServerCheck = false;
    private Calendar mLastServerCheck = Calendar.getInstance();
    private GeofencingClient mGeofencingClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private PendingIntent mGeofencePendingIntent;
    private LocationCallback mLocationCallback;

    public GpsManager(Context context) {
        Injector.INSTANCE.initGpsManager(this);
        mApplication = context;
    }

    public void start() {
        if (mIsManagerActive || mIsManagerStarting)
            return;

        mIsManagerStarting = true;
        initGpsService();
        startGpsService();
        VisilabsAlarm.getSingleton().setAlarmCheckIn(mApplication);

    }

    private void initGpsService() {
        mGeofencingClient = LocationServices.getGeofencingClient(mApplication);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mApplication);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        setLastKnownLocation(location);
                    }
                }
            }
        };
    }

    @SuppressLint("MissingPermission")
    public void startGpsService() {

        boolean accessFineLocationPermission = ContextCompat.checkSelfPermission(mApplication,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean accessCoarseLocationPermission = ContextCompat.checkSelfPermission(mApplication,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (accessFineLocationPermission || accessCoarseLocationPermission) {

            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        setLastKnownLocation(location);
                    } else {
                        LocationRequest locationRequest = LocationRequest.create();
                        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                        locationRequest.setInterval(10000);
                        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());
                    }
                }
            });
        }
    }

    private boolean GeoFenceEntitiesAreTheSame(VisilabsGeoFenceEntity geoFenceEntity1, VisilabsGeoFenceEntity geoFenceEntity2) {
        if (!geoFenceEntity1.getLatitude().equals(geoFenceEntity2.getLatitude()))
            return false;

        if (!geoFenceEntity1.getLongitude().equals(geoFenceEntity2.getLongitude()))
            return false;

        if (geoFenceEntity1.getRadius() != geoFenceEntity2.getRadius())
            return false;

        if (!geoFenceEntity1.getName().equals(geoFenceEntity2.getName()))
            return false;

        return geoFenceEntity1.getType().equals(geoFenceEntity2.getType());
    }

    public void setLastKnownLocation(Location location) {

        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

        SharedPreferences prefs = mApplication.getSharedPreferences(VisilabsConstant.GEOFENCE_INTERVAL_NAME, Activity.MODE_PRIVATE);
        int interval = prefs.getInt(VisilabsConstant.GEOFENCE_INTERVAL_KEY, -1);

        if(interval == -1) {
            interval = 15;
        }

        interval = interval * -1;

        Calendar minutesBefore = Calendar.getInstance(); // current date/time
        minutesBefore.add(Calendar.MINUTE, interval);

        if (mLastKnownLocation == null && location == null)
            return;

        if (mLastKnownLocation == null) {
            mLastKnownLocation = location;


        } else {
            if (location != null) {
                double lat1 = mLastKnownLocation.getLatitude();
                double long1 = mLastKnownLocation.getLongitude();
                double lat2 = location.getLatitude();
                double long2 = location.getLongitude();
                if (GeoFencesUtils.haversine(lat1, long1, lat2, long2) > 1) {
                    mLastKnownLocation = location;
                }
            }
        }
        if (!mFirstServerCheck || mLastServerCheck.before(minutesBefore)) {
            setupGeofences();
            mLastServerCheck = Calendar.getInstance();
            mFirstServerCheck = true;
        }
    }

    private void setupGeofences() {
        final VisilabsGeofenceRequest request = new VisilabsGeofenceRequest(mApplication);
        double lat = mLastKnownLocation.getLatitude();
        double lon = mLastKnownLocation.getLongitude();

        request.setLatitude(lat);
        request.setLongitude(lon);

        request.setAction("getlist");
        request.setApiVer("Android");
        final VisilabsGeofenceGetListCallback callback = new VisilabsGeofenceGetListCallback() {
            @Override
            public void success(List<VisilabsGeofenceGetListResponse> response, String url) {
                Log.i(TAG, "Success Request : " + url);

                List<VisilabsGeoFenceEntity> geoFences = new ArrayList<>();

                if(response != null && response.size() != 0) {
                    for (int i = 0; i < response.size(); i++) {
                        VisilabsGeofenceGetListResponse visilabsGeofenceGetListResponse = response.get(i);
                        for(int j = 0 ; j < visilabsGeofenceGetListResponse.getGeofences().size() ; j++) {
                            VisilabsGeoFenceEntity visilabsGeoFenceEntity = new VisilabsGeoFenceEntity();
                            visilabsGeoFenceEntity.setGuid(visilabsGeofenceGetListResponse.getActId() +
                                    "_" + j + "_" + visilabsGeofenceGetListResponse.getGeofences().get(j).getId());
                            visilabsGeoFenceEntity.setLatitude(Double.toString(visilabsGeofenceGetListResponse.
                                    getGeofences().get(j).getLatitude()));
                            visilabsGeoFenceEntity.setLongitude(Double.toString(visilabsGeofenceGetListResponse.
                                    getGeofences().get(j).getLongitude()));
                            visilabsGeoFenceEntity.setRadius(visilabsGeofenceGetListResponse.
                                    getGeofences().get(j).getRadius().intValue());
                            visilabsGeoFenceEntity.setType(visilabsGeofenceGetListResponse.getTrgevt());
                            visilabsGeoFenceEntity.setDurationInSeconds(visilabsGeofenceGetListResponse.getDistance());
                            visilabsGeoFenceEntity.setGeoid(visilabsGeofenceGetListResponse.getGeofences().get(j).getId());

                            geoFences.add(visilabsGeoFenceEntity);
                        }
                    }
                    setupGeofencesCallback(geoFences);
                }
            }

            @Override
            public void fail(Throwable t, String url) {
                Log.e(TAG, "Fail Request : " + url);
                Log.e(TAG, "Fail Request Message : " + t.getMessage());
            }
        };
        try {
            request.executeAsync(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupGeofencesCallback(List<VisilabsGeoFenceEntity> geoFences) {
        if (geoFences == null) {
            return;
        }

        mAllGeoFenceEntityList.clear();
        mAllGeoFenceEntityList.addAll(geoFences);

        double lat1 = mLastKnownLocation.getLatitude();
        double long1 = mLastKnownLocation.getLongitude();
        for (VisilabsGeoFenceEntity entity : mAllGeoFenceEntityList) {
            entity.setDistance(GeoFencesUtils.haversine(lat1, long1, Double.parseDouble(entity.getLatitude()),
                    Double.parseDouble(entity.getLongitude()))); //difference btw two points
        }
        Collections.sort(mAllGeoFenceEntityList, new DistanceComparator());
        mToAddGeoFenceEntityList.clear();
        mToRemoveGeoFenceEntityList.clear();


        if (!mActiveGeoFenceEntityList.isEmpty()) {
            if (mGeofencingClient != null)
                removeGeofences(mActiveGeoFenceEntityList);
            mActiveGeoFenceEntityList.clear();
        }

        if (mActiveGeoFenceEntityList.isEmpty()) {
            if (mAllGeoFenceEntityList.size() > 100) {
                mToAddGeoFenceEntityList.addAll(mAllGeoFenceEntityList.subList(0, 100));
            } else {
                mToAddGeoFenceEntityList.addAll(mAllGeoFenceEntityList);
            }
        }

        if (mGeofencingClient == null)
            return;

        if (!mToRemoveGeoFenceEntityList.isEmpty()) {
            removeGeofences(mToRemoveGeoFenceEntityList);
            Iterator<VisilabsGeoFenceEntity> it = mActiveGeoFenceEntityList.iterator();
            while (it.hasNext()) {
                VisilabsGeoFenceEntity geofence = it.next();
                for (VisilabsGeoFenceEntity entityToRemove : mToRemoveGeoFenceEntityList) {
                    if (GeoFenceEntitiesAreTheSame(geofence, entityToRemove)) {
                        it.remove();
                    }
                }
            }
        }

        if (!mToAddGeoFenceEntityList.isEmpty()) {
            addGeofences(mToAddGeoFenceEntityList);
            mActiveGeoFenceEntityList.addAll(mToAddGeoFenceEntityList);
        }
    }

    private void removeGeofences(final List<VisilabsGeoFenceEntity> geoFencesToRemove) {
        final List<String> IdsToRemove = new ArrayList<>();
        for (VisilabsGeoFenceEntity geofenceEntity : geoFencesToRemove) {
            IdsToRemove.add(geofenceEntity.getGuid());
        }

        if (IdsToRemove.isEmpty())
            return;

        mGeofencingClient.removeGeofences(IdsToRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                VisilabsLog.v(TAG, "Removing geofences success ");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        VisilabsLog.e(TAG, "Removing geofence failed: " + e.getMessage(), e);
                    }
                });

    }

    private GeofencingRequest getAddGeofencingRequest(List<Geofence> geofences) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.addGeofences(geofences);
        return builder.build();
    }

    @SuppressLint("MissingPermission")
    private void addGeofences(final List<VisilabsGeoFenceEntity> geoFencesToAdd) {
        List<Geofence> geofences = new ArrayList<>();
        for (VisilabsGeoFenceEntity geoFenceEntity : geoFencesToAdd) {
            Geofence newGf = geoFenceEntity.toGeofence();
            geofences.add(newGf);
        }

        boolean accessFineLocationPermission = ContextCompat.checkSelfPermission(mApplication,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean accessCoarseLocationPermission = ContextCompat.checkSelfPermission(mApplication,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (accessFineLocationPermission || accessCoarseLocationPermission) {
            mGeofencingClient.addGeofences(getAddGeofencingRequest(geofences), getGeofencePendingIntent())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            VisilabsLog.v(TAG, "Registering geofence success ");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            VisilabsLog.e(TAG, "Registering geofence failed: " + e.getMessage(), e);
                        }
                    });
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(mApplication, GeofenceBroadcastReceiver.class);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            mGeofencePendingIntent = PendingIntent.getBroadcast(mApplication, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            mGeofencePendingIntent = PendingIntent.getBroadcast(mApplication, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return mGeofencePendingIntent;
    }

    private static class DistanceComparator implements Comparator<VisilabsGeoFenceEntity> {
        @Override
        public int compare(VisilabsGeoFenceEntity object1, VisilabsGeoFenceEntity object2) {
            double position1, position2;
            position1 = object1.getDistance();
            position2 = object2.getDistance();
            return Double.compare(position1, position2);
        }
    }

    public Location getLastKnownLocation() {
        return mLastKnownLocation;
    }
}
