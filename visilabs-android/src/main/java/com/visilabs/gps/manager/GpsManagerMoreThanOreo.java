package com.visilabs.gps.manager;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.visilabs.Injector;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsGeofenceRequest;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.gps.entities.VisilabsGeoFenceEntity;
import com.visilabs.gps.geofence.GeofenceBroadcastReceiver;
import com.visilabs.gps.geofence.VisilabsGeofenceTriggerAlarm;
import com.visilabs.gps.util.GeoFencesUtils;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;
import com.visilabs.util.VisilabsLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class GpsManagerMoreThanOreo {

    private final String TAG = "Visilabs GpsManager2";
    public final List<VisilabsGeoFenceEntity> activeGeoFenceEntityList = new ArrayList<>();
    private final List<VisilabsGeoFenceEntity> allGeoFenceEntityList = new ArrayList<>();
    private final List<VisilabsGeoFenceEntity> toAddGeoFenceEntityList = new ArrayList<>();
    private final List<VisilabsGeoFenceEntity> toRemoveGeoFenceEntityList = new ArrayList<>();
    private final Context application;
    public boolean isManagerActive = false;
    public boolean isManagerStarting = false;
    private Location lastKnownLocation = null;
    private boolean firstServerCheck = false;
    private Calendar lastServerCheck = Calendar.getInstance();
    private GeofencingClient mGeofencingClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private PendingIntent mGeofencePendingIntent;

    public GpsManagerMoreThanOreo(Context context) {
        Injector.INSTANCE.initGpsManager(this);
        this.application = context;
    }

    public void start() {
        if (isManagerActive || isManagerStarting)
            return;

        isManagerStarting = true;
        initGpsService();
        startGpsService();
        VisilabsGeofenceTriggerAlarm.getSingleton().setAlarmCheckIn(this.application);

    }

    private void initGpsService() {
        mGeofencingClient = LocationServices.getGeofencingClient(this.application);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.application);
    }

    public void startGpsService() {

        boolean accessFineLocationPermission = ContextCompat.checkSelfPermission(this.application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean accessCoarseLocationPermission = ContextCompat.checkSelfPermission(this.application, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (accessFineLocationPermission || accessCoarseLocationPermission) {

            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        setLastKnownLocation(location);
                    }
                }
            });
        }
    }

    private boolean GeoFenceEntitiesAreTheSame(VisilabsGeoFenceEntity geoFenceEntity1, VisilabsGeoFenceEntity geoFenceEntity2) {
        if (!geoFenceEntity1.lat.equals(geoFenceEntity2.lat))
            return false;

        if (!geoFenceEntity1.lng.equals(geoFenceEntity2.lng))
            return false;

        if (geoFenceEntity1.radius != geoFenceEntity2.radius)
            return false;

        if (!geoFenceEntity1.name.equals(geoFenceEntity2.name))
            return false;

        if (!geoFenceEntity1.type.equals(geoFenceEntity2.type))
            return false;

        return true;
    }

    public void setLastKnownLocation(Location location) {

        Calendar fifteenMinutesBefore = Calendar.getInstance(); // current date/time
        fifteenMinutesBefore.add(Calendar.MINUTE, -15);

        if (lastKnownLocation == null && location == null)
            return;

        if (lastKnownLocation == null) {
            lastKnownLocation = location;


            if (!firstServerCheck || lastServerCheck.before(fifteenMinutesBefore)) {
                setupGeofences();
                lastServerCheck = Calendar.getInstance();
                firstServerCheck = true;
            }

        } else {
            if (location == null) {
                if (!firstServerCheck || lastServerCheck.before(fifteenMinutesBefore)) {
                    setupGeofences();
                    lastServerCheck = Calendar.getInstance();
                    firstServerCheck = true;
                }
            } else {
                double lat1 = lastKnownLocation.getLatitude();
                double long1 = lastKnownLocation.getLongitude();
                double lat2 = location.getLatitude();
                double long2 = location.getLongitude();
                if (GeoFencesUtils.haversine(lat1, long1, lat2, long2) > 1) {
                    lastKnownLocation = location;
                }
                if (!firstServerCheck || lastServerCheck.before(fifteenMinutesBefore)) {
                    setupGeofences();
                    lastServerCheck = Calendar.getInstance();
                    firstServerCheck = true;
                }
            }
        }
    }

    private void setupGeofences() {
        final VisilabsGeofenceRequest request = (VisilabsGeofenceRequest) new VisilabsGeofenceRequest(this.application);
        double lat = lastKnownLocation.getLatitude();
        double lon = lastKnownLocation.getLongitude();

        request.setLatitude(lat);
        request.setLongitude(lon);

        request.setAction("getlist");
        request.setApiVer("Android");
        final VisilabsCallback callback = new VisilabsCallback() {
            @Override
            public void success(VisilabsResponse response) {
                String rawResponse = response.getRawResponse();
                try {
                    List<VisilabsGeoFenceEntity> geoFences = new ArrayList<>();
                    JSONArray array = response.getArray();
                    if (array != null) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject action = (JSONObject) array.get(i);
                            String trgevt = action.getString("trgevt");
                            int actid = action.getInt("actid");
                            int dis = action.getInt("dis");
                            JSONArray geoJsons = action.getJSONArray("geo");
                            if (geoJsons != null) {
                                for (int j = 0; j < geoJsons.length(); j++) {
                                    JSONObject geoJson = (JSONObject) geoJsons.get(j);
                                    double latitude = geoJson.getDouble("lat");
                                    double longitude = geoJson.getDouble("long");
                                    int radius = geoJson.getInt("rds");
                                    int geoid = geoJson.getInt("id");

                                    VisilabsGeoFenceEntity visilabsGeoFenceEntity = new VisilabsGeoFenceEntity();
                                    visilabsGeoFenceEntity.guid = actid + "_" + j + "_" + geoid;
                                    visilabsGeoFenceEntity.lat = Double.toString(latitude);
                                    visilabsGeoFenceEntity.lng = Double.toString(longitude);
                                    visilabsGeoFenceEntity.radius = radius;
                                    visilabsGeoFenceEntity.type = trgevt;
                                    visilabsGeoFenceEntity.durationInSeconds = dis;
                                    visilabsGeoFenceEntity.geoid = geoid;

                                    geoFences.add(visilabsGeoFenceEntity);
                                }
                            }
                        }
                        setupGeofencesCallback(geoFences);
                    }

                } catch (Exception ex) {
                    Log.e("VL", ex.toString());
                }
            }

            @Override
            public void fail(VisilabsResponse response) {
                String rawResponse = response.getRawResponse();
                try {
                    JSONArray array = response.getArray();
                } catch (Exception ex) {
                    Log.e("VL", ex.toString());
                }
            }
        };
        try {
            if (Looper.myLooper() == null)
                Looper.prepare();
            Handler mainHandler = new Handler(Looper.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        request.executeAsync(callback);
                    } catch (Exception e) {
                        Log.e("VL", e.toString());
                    }
                }
            };
            mainHandler.post(myRunnable);
            if (Looper.myLooper() == null)
                Looper.loop();


        } catch (Exception ex) {
            VisilabsLog.e(TAG, ex.getMessage(), ex);
        }

    }

    private void setupGeofencesCallback(List<VisilabsGeoFenceEntity> geoFences) {
        if (geoFences == null) {
            return;
        }

        allGeoFenceEntityList.clear();
        for (VisilabsGeoFenceEntity visilabsGeoFenceEntity : geoFences) {
            allGeoFenceEntityList.add(visilabsGeoFenceEntity);
        }

        double lat1 = lastKnownLocation.getLatitude();
        double long1 = lastKnownLocation.getLongitude();
        for (VisilabsGeoFenceEntity entity : allGeoFenceEntityList) {
            entity.distance = GeoFencesUtils.haversine(lat1, long1, Double.parseDouble(entity.lat), Double.parseDouble(entity.lng));
        }
        Collections.sort(allGeoFenceEntityList, new GpsManagerMoreThanOreo.DistanceComparator());
        toAddGeoFenceEntityList.clear();
        toRemoveGeoFenceEntityList.clear();


        if (!activeGeoFenceEntityList.isEmpty()) {
            if (mGeofencingClient != null)
                removeGeofences(activeGeoFenceEntityList);
            activeGeoFenceEntityList.clear();
        }

        if (activeGeoFenceEntityList.isEmpty()) {
            if (allGeoFenceEntityList.size() > 100) {
                toAddGeoFenceEntityList.addAll(allGeoFenceEntityList.subList(0, 100));
            } else {
                toAddGeoFenceEntityList.addAll(allGeoFenceEntityList);
            }
        }

        if (mGeofencingClient == null)
            return;

        if (!toRemoveGeoFenceEntityList.isEmpty()) {
            removeGeofences(toRemoveGeoFenceEntityList);
            Iterator<VisilabsGeoFenceEntity> it = activeGeoFenceEntityList.iterator();
            while (it.hasNext()) {
                VisilabsGeoFenceEntity geofence = it.next();
                for (VisilabsGeoFenceEntity entityToRemove : toRemoveGeoFenceEntityList) {
                    if (GeoFenceEntitiesAreTheSame(geofence, entityToRemove)) {
                        it.remove();
                    }
                }
            }
        }

        if (!toAddGeoFenceEntityList.isEmpty()) {
            addGeofences(toAddGeoFenceEntityList);
            activeGeoFenceEntityList.addAll(toAddGeoFenceEntityList);
        }

    }

    private void removeGeofences(final List<VisilabsGeoFenceEntity> geoFencesToRemove) {
        final List<String> IdsToRemove = new ArrayList<>();
        for (VisilabsGeoFenceEntity geofenceEntity : geoFencesToRemove) {
            IdsToRemove.add(geofenceEntity.guid);
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

    private void addGeofences(final List<VisilabsGeoFenceEntity> geoFencesToAdd) {
        final List<Geofence> geofences = new ArrayList<>();
        for (VisilabsGeoFenceEntity geoFenceEntity : geoFencesToAdd) {
            Geofence newGf = geoFenceEntity.toGeofence();
            geofences.add(newGf);
        }

        boolean accessFineLocationPermission = ContextCompat.checkSelfPermission(this.application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (accessFineLocationPermission) {
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
        Intent intent = new Intent(this.application, GeofenceBroadcastReceiver.class);
        mGeofencePendingIntent = PendingIntent.getBroadcast(this.application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }


    private class DistanceComparator implements Comparator<VisilabsGeoFenceEntity> {
        @Override
        public int compare(VisilabsGeoFenceEntity object1, VisilabsGeoFenceEntity object2) {
            double position1, position2;
            position1 = object1.distance;
            position2 = object2.distance;
            return Double.compare(position1, position2);
        }
    }

}
