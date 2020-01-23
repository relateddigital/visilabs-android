package com.visilabs.android.gps.manager;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.visilabs.android.VisilabsResponse;
import com.visilabs.android.api.VisilabsGeofenceRequest;
import com.visilabs.android.api.VisilabsTargetCallback;
import com.visilabs.android.Injector;
import com.visilabs.android.gps.entities.VisilabsGeoFenceEntity;
import com.visilabs.android.gps.geofence.GeofenceMonitor;
import com.visilabs.android.gps.geofence.GeofenceMonitorConnection;
import com.visilabs.android.gps.listener.IVisilabsGeofenceListener;
import com.visilabs.android.gps.util.GeoFencesUtils;
import com.visilabs.android.json.JSONArray;
import com.visilabs.android.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class GpsManager {
    private final String TAG = "Visilabs GpsManager";
    public final List<VisilabsGeoFenceEntity> activeGeoFenceEntityList = new ArrayList<>();
    private final List<VisilabsGeoFenceEntity> allGeoFenceEntityList = new ArrayList<>();
    private final List<VisilabsGeoFenceEntity> toAddGeoFenceEntityList = new ArrayList<>();
    private final List<VisilabsGeoFenceEntity> toRemoveGeoFenceEntityList = new ArrayList<>();
    private final Context application;
    private boolean isManagerActive = false;
    private boolean isManagerStarting = false;
    private Location lastKnownLocation = null;
    private IVisilabsGeofenceListener geofenceListener = null;
    private String mode = "foreground";
    private long lastUpdate = 0;
    private Intent gpsServiceIntent;
    private GeofenceMonitor visilabsGpsService;
    private GeofenceMonitorConnection visilabsGpsServiceConnection;


    private boolean firstServerCheck = false;
    private Calendar lastServerCheck = Calendar.getInstance();

    public GpsManager(Context context) {
        Injector.INSTANCE.initGpsManager(this);
        this.application = context; //Visilabs.CallAPI().getContext();
    }

    public void start() {
        if (isManagerActive || isManagerStarting)
            return;

        isManagerStarting = true;
        initGpsService();
        startGpsService();
        bindGpsService();
    }

    private void initGpsService() {
        gpsServiceIntent = new Intent(application, GeofenceMonitor.class);
    }

    private void startGpsService() {
        application.startService(gpsServiceIntent);
    }

    private void bindGpsService() {
        visilabsGpsServiceConnection = new GeofenceMonitorConnection();
        application.bindService(gpsServiceIntent, visilabsGpsServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void setGeoMonitorReference(GeofenceMonitor geoMonitor) {
        visilabsGpsService = geoMonitor;
    }

    private void unbindGpsService() {
        if (visilabsGpsService != null) {
            try {
                application.unbindService(visilabsGpsServiceConnection);
            } catch (IllegalArgumentException e) {
            }
            visilabsGpsService = null;
        }
    }

    public void stopGpsService() {
        if (gpsServiceIntent != null) {
            //Log.e(TAG, "stopBeaconService");
            unbindGpsService();
            application.stopService(gpsServiceIntent);
            gpsServiceIntent = null;
            isManagerStarting = false;
            isManagerActive = false;
        }
    }

    public boolean getManagerState() {
        return isManagerActive;
    }

    public void setManagerState(boolean _gpsState) {
        //Logs.showLog(Logs.LOG_GPS_MANAGER, "Setting GPS state to " + _gpsState);
        isManagerStarting = false;
        isManagerActive = _gpsState;
    }

    public void setForeground() {
        mode = "foreground";
    }

    public void setBackground() {
        mode = "background";
    }

    public void updateGeofences() {
        //Logs.showLog(Logs.LOG_GPS_MANAGER, "Updating Geofences");
        setLastKnownLocation(null);
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

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location location) {

        Calendar fifteenMinutesBefore = Calendar.getInstance(); // current date/time
        fifteenMinutesBefore.add(Calendar.MINUTE, -15);

        if (lastKnownLocation == null && location == null)
            return;

        if (lastKnownLocation == null) {
            lastKnownLocation = location;


            if(!firstServerCheck || lastServerCheck.before(fifteenMinutesBefore)){
                setupGeofences();
                lastServerCheck = Calendar.getInstance();
                firstServerCheck = true;
            }


        } else {
            if (location == null) {
                //setupGeofences();
                if(!firstServerCheck || lastServerCheck.before(fifteenMinutesBefore)){
                    setupGeofences();
                    lastServerCheck = Calendar.getInstance();
                    firstServerCheck = true;
                }
            } else {
                double lat1 = lastKnownLocation.getLatitude();
                double long1 = lastKnownLocation.getLongitude();
                double lat2 = location.getLatitude();
                double long2 = location.getLongitude();
                //if (mode.equals("foreground") && Utils.haversine(lat1, long1, lat2, long2) > 0.025) {
                //saveLocationEvent(location);
                //}
                if (GeoFencesUtils.haversine(lat1, long1, lat2, long2) > 1) {
                    lastKnownLocation = location;
                    //setupGeofences();
                }
                //setupGeofences();
                if(!firstServerCheck || lastServerCheck.before(fifteenMinutesBefore)){
                    setupGeofences();
                    lastServerCheck = Calendar.getInstance();
                    firstServerCheck = true;
                }
            }
        }
    }

    /*
    private void saveLocationEvent(Location location) {
        if (location == null)
            return;
        if (System.currentTimeMillis() / 1000 - lastUpdate < 60) {
            //Logs.showLog(Logs.LOG_GPS_MANAGER, "Wait 1 minute");
            return;
        }

        lastUpdate = System.currentTimeMillis() / 1000;
        //Logs.showLog(Logs.LOG_GPS_MANAGER, "saveLocationEvent");
        Realm realm = Realm.getInstance(Injector.INSTANCE.getAppContextComponent().realmConfiguration());
        realm.refresh();
        PulRealmGeoLocationEntities entity = new PulRealmGeoLocationEntities();
        entity.setType(mode);
        entity.setLat("" + location.getLatitude());
        entity.setLng("" + location.getLongitude());
        entity.setOccurredAt("" + System.currentTimeMillis() / 1000);
        realm.beginTransaction();
        realm.copyToRealm(entity);
        realm.commitTransaction();
        realm.close();
        System.gc();
    }
    */

    private void setupGeofences() {
        VisilabsGeofenceRequest request = (VisilabsGeofenceRequest) new VisilabsGeofenceRequest(this.application);
        double lat = lastKnownLocation.getLatitude();
        double lon = lastKnownLocation.getLongitude();

        request.setLatitude(lat);
        request.setLongitude(lon);

        request.setAction("getlist");
        request.setApiVer("Android");
        VisilabsTargetCallback callback = new VisilabsTargetCallback() {
            @Override
            public void success(VisilabsResponse response) {
                String rawResponse = response.getRawResponse();
                try{
                    List<VisilabsGeoFenceEntity> geoFences = new ArrayList<>();
                    JSONArray array =  response.getArray();
                    if(array != null) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject action  = (JSONObject)array.get(i);
                            String trgevt      = action.getString("trgevt");
                            int actid          = action.getInt("actid");
                            int dis            = action.getInt("dis");
                            JSONArray geoJsons = action.getJSONArray("geo");
                            if(geoJsons != null){
                                for (int j = 0; j < geoJsons.length(); j++) {
                                    JSONObject geoJson = (JSONObject)geoJsons.get(j);
                                    double latitude = geoJson.getDouble("lat");
                                    double longitude = geoJson.getDouble("long");
                                    int radius = geoJson.getInt("rds");

                                    VisilabsGeoFenceEntity visilabsGeoFenceEntity = new VisilabsGeoFenceEntity();
                                    visilabsGeoFenceEntity.guid = actid + "_" + j;
                                    visilabsGeoFenceEntity.lat = Double.toString(latitude);
                                    visilabsGeoFenceEntity.lng = Double.toString(longitude);
                                    visilabsGeoFenceEntity.radius = radius;
                                    visilabsGeoFenceEntity.type = trgevt;
                                    visilabsGeoFenceEntity.durationInSeconds = dis;

                                    geoFences.add(visilabsGeoFenceEntity);
                                }
                            }
                        }
                        setupGeofencesCallback(geoFences);
                    }

                }
                catch (Exception ex)
                {

                }
            }

            @Override
            public void fail(VisilabsResponse response) {
                String rawResponse = response.getRawResponse();
                try{
                    JSONArray array =  response.getArray();
                }catch (Exception ex){

                }
            }
        };
        try {
            request.executeAsync(callback);
        }catch (Exception ex){

        }

    }

    private void setupGeofencesCallback(List<VisilabsGeoFenceEntity> geoFences){
        if(geoFences == null){
            return;
        }

        allGeoFenceEntityList.clear();
        for (VisilabsGeoFenceEntity visilabsGeoFenceEntity: geoFences){
            allGeoFenceEntityList.add(visilabsGeoFenceEntity);
        }

        double lat1 = lastKnownLocation.getLatitude();
        double long1 = lastKnownLocation.getLongitude();
        for (VisilabsGeoFenceEntity entity : allGeoFenceEntityList) {
            entity.distance = GeoFencesUtils.haversine(lat1, long1, Double.parseDouble(entity.lat), Double.parseDouble(entity.lng));
        }
        Collections.sort(allGeoFenceEntityList, new DistanceComparator());
        toAddGeoFenceEntityList.clear();
        toRemoveGeoFenceEntityList.clear();


        if (!activeGeoFenceEntityList.isEmpty()) {
            if (visilabsGpsService != null)
                visilabsGpsService.removeGeofences(activeGeoFenceEntityList);
            activeGeoFenceEntityList.clear();
        }


        if (activeGeoFenceEntityList.isEmpty()) {
            if (allGeoFenceEntityList.size() > 20) {
                toAddGeoFenceEntityList.addAll(allGeoFenceEntityList.subList(0, 20));
            } else {
                toAddGeoFenceEntityList.addAll(allGeoFenceEntityList);
            }
        }/* else {
            if (allGeoFenceEntityList.size() > 20) {
                List<VisilabsGeoFenceEntity> temporarySubList = allGeoFenceEntityList.subList(0, 20);
                for (VisilabsGeoFenceEntity entity1 : activeGeoFenceEntityList) {
                    if (!checkIfListContains(entity1, temporarySubList)) {
                        toRemoveGeoFenceEntityList.add(entity1);
                    }
                }

                for (VisilabsGeoFenceEntity entity1 : temporarySubList) {
                    if (!checkIfListContains(entity1, activeGeoFenceEntityList)) {
                        toAddGeoFenceEntityList.add(entity1);
                    }
                }
            }
        }*/

        if (visilabsGpsService == null)
            return;

        if (!toRemoveGeoFenceEntityList.isEmpty()) {
            visilabsGpsService.removeGeofences(toRemoveGeoFenceEntityList);
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
            visilabsGpsService.addGeofences(toAddGeoFenceEntityList);
            //visilabsGpsService.setupPolygonGeofences();
            activeGeoFenceEntityList.addAll(toAddGeoFenceEntityList);
        }

    }


    private boolean checkIfListContains(VisilabsGeoFenceEntity entity, List<VisilabsGeoFenceEntity> listToCheck) {
        for (VisilabsGeoFenceEntity geofence : listToCheck) {
            if (GeoFenceEntitiesAreTheSame(entity, geofence))
                return true;
        }
        return false;
    }

    public void setHighPowerGPS() {
        if (visilabsGpsService != null)
            visilabsGpsService.setHighPowerGPS();
    }

    public void setMediumPowerGPS() {
        if (visilabsGpsService != null)
            visilabsGpsService.setMediumPowerGPS();
    }

    public void setLowPowerGPS() {
        if (visilabsGpsService != null)
            visilabsGpsService.setLowPowerGPS();
    }

    public void setNoPowerGPS() {
        if (visilabsGpsService != null)
            visilabsGpsService.setNoPowerGPS();
    }

    public IVisilabsGeofenceListener getListener() {
        return geofenceListener;
    }

    public void setListener(IVisilabsGeofenceListener _geofenceListener) {
        this.geofenceListener = _geofenceListener;
    }

    public Location getLastFusedLocation() {
        if (visilabsGpsService != null)
            return visilabsGpsService.getLastFusedLocation();
        Log.e(TAG, "geofenceMonitor null");
        return null;
    }

    private class DistanceComparator implements Comparator<VisilabsGeoFenceEntity> {
        @Override
        public int compare(VisilabsGeoFenceEntity object1, VisilabsGeoFenceEntity object2) {
            double position1 = 0, position2 = 0;
            position1 = object1.distance;
            position2 = object2.distance;
            return Double.compare(position1, position2);
        }
    }
}

