package com.visilabs.gps.manager;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsGeofenceGetListCallback;
import com.visilabs.api.VisilabsGeofenceRequest;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.Injector;
import com.visilabs.gps.entities.VisilabsGeoFenceEntity;
import com.visilabs.gps.geofence.GeofenceMonitor;
import com.visilabs.gps.geofence.GeofenceMonitorConnection;
import com.visilabs.gps.listener.IVisilabsGeofenceListener;
import com.visilabs.gps.model.VisilabsGeofenceGetListResponse;
import com.visilabs.gps.util.GeoFencesUtils;
import com.visilabs.json.JSONArray;
import com.visilabs.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class GpsManager {
    private final String TAG = "Visilabs GpsManager";
    public final List<VisilabsGeoFenceEntity> mActiveGeoFenceEntityList = new ArrayList<>();
    private final List<VisilabsGeoFenceEntity> mAllGeoFenceEntityList = new ArrayList<>();
    private final List<VisilabsGeoFenceEntity> mToAddGeoFenceEntityList = new ArrayList<>();
    private final List<VisilabsGeoFenceEntity> mToRemoveGeoFenceEntityList = new ArrayList<>();
    private final Context mApplication;
    private final boolean mIsManagerActive = false;
    private boolean mIsManagerStarting = false;
    private Location mLastKnownLocation = null;
    private final IVisilabsGeofenceListener mGeofenceListener = null;
    private Intent mGpsServiceIntent;
    private GeofenceMonitor mVisilabsGpsService;
    private GeofenceMonitorConnection mVisilabsGpsServiceConnection;
    private boolean mFirstServerCheck = false;
    private Calendar mLastServerCheck = Calendar.getInstance();

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
        bindGpsService();
    }

    private void initGpsService() {
        mGpsServiceIntent = new Intent(mApplication, GeofenceMonitor.class);
    }

    private void startGpsService() {
        mApplication.startService(mGpsServiceIntent);
    }

    private void bindGpsService() {
        mVisilabsGpsServiceConnection = new GeofenceMonitorConnection();
        mApplication.bindService(mGpsServiceIntent, mVisilabsGpsServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void setGeoMonitorReference(GeofenceMonitor geoMonitor) {
        mVisilabsGpsService = geoMonitor;
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

        Calendar fifteenMinutesBefore = Calendar.getInstance(); // current date/time
        fifteenMinutesBefore.add(Calendar.MINUTE, -15);

        if (mLastKnownLocation == null && location == null)
            return;

        if (mLastKnownLocation == null) {
            mLastKnownLocation = location;


            if(!mFirstServerCheck || mLastServerCheck.before(fifteenMinutesBefore)){
                setupGeofences();
                mLastServerCheck = Calendar.getInstance();
                mFirstServerCheck = true;
            }


        } else {
            if (location == null) {
                if(!mFirstServerCheck || mLastServerCheck.before(fifteenMinutesBefore)){
                    setupGeofences();
                    mLastServerCheck = Calendar.getInstance();
                    mFirstServerCheck = true;
                }
            } else {
                double lat1 = mLastKnownLocation.getLatitude();
                double long1 = mLastKnownLocation.getLongitude();
                double lat2 = location.getLatitude();
                double long2 = location.getLongitude();
                if (GeoFencesUtils.haversine(lat1, long1, lat2, long2) > 1) {
                    mLastKnownLocation = location;
                }
                if(!mFirstServerCheck || mLastServerCheck.before(fifteenMinutesBefore)){
                    setupGeofences();
                    mLastServerCheck = Calendar.getInstance();
                    mFirstServerCheck = true;
                }
            }
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

    private void setupGeofencesCallback(List<VisilabsGeoFenceEntity> geoFences){
        if(geoFences == null){
            return;
        }

        mAllGeoFenceEntityList.clear();
        mAllGeoFenceEntityList.addAll(geoFences);

        double lat1 = mLastKnownLocation.getLatitude();
        double long1 = mLastKnownLocation.getLongitude();
        for (VisilabsGeoFenceEntity entity : mAllGeoFenceEntityList) {
            entity.setDistance(GeoFencesUtils.haversine(lat1, long1, Double.parseDouble(entity.getLatitude()),
                    Double.parseDouble(entity.getLongitude())));
        }
        Collections.sort(mAllGeoFenceEntityList, new DistanceComparator());
        mToAddGeoFenceEntityList.clear();
        mToRemoveGeoFenceEntityList.clear();


        if (!mActiveGeoFenceEntityList.isEmpty()) {
            if (mVisilabsGpsService != null)
                mVisilabsGpsService.removeGeofences(mActiveGeoFenceEntityList);
            mActiveGeoFenceEntityList.clear();
        }


        if (mActiveGeoFenceEntityList.isEmpty()) {
            if (mAllGeoFenceEntityList.size() > 20) {
                mToAddGeoFenceEntityList.addAll(mAllGeoFenceEntityList.subList(0, 20));
            } else {
                mToAddGeoFenceEntityList.addAll(mAllGeoFenceEntityList);
            }
        }

        if (mVisilabsGpsService == null)
            return;

        if (!mToRemoveGeoFenceEntityList.isEmpty()) {
            mVisilabsGpsService.removeGeofences(mToRemoveGeoFenceEntityList);
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
            mVisilabsGpsService.addGeofences(mToAddGeoFenceEntityList);
            mActiveGeoFenceEntityList.addAll(mToAddGeoFenceEntityList);
        }

    }

    public IVisilabsGeofenceListener getListener() {
        return mGeofenceListener;
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

