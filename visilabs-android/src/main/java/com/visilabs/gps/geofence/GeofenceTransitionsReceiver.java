package com.visilabs.gps.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.visilabs.Injector;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsGeofenceRequest;
import com.visilabs.api.VisilabsCallback;
import com.visilabs.gps.entities.VisilabsGeoFenceEntity;
import com.visilabs.gps.manager.GpsManager;
import com.visilabs.gps.util.GeoFencesUtils;

import java.util.List;

public class GeofenceTransitionsReceiver extends BroadcastReceiver {
    private static final String TAG = "GeoTransitionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Visilabs.CallAPI() == null) {
            Visilabs.CreateAPI(context.getApplicationContext());
        }

        Visilabs.CallAPI().startGpsManager();

        setGeofenceEvent(intent);
    }

    private void setGeofenceEvent(Intent intent) {
        GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(intent);
        if (geoFenceEvent.hasError()) {
            int errorCode = geoFenceEvent.getErrorCode();
            Log.e("geoFenceEvent", String.valueOf(errorCode));
        } else {

            GpsManager gpsManager = Injector.INSTANCE.getGpsManager();

            if (gpsManager != null) {
                List<Geofence> triggerList = geoFenceEvent.getTriggeringGeofences();
                Geofence closestGeofence = getClosestTriggeredGoefence(gpsManager, triggerList);

                try {
                    geoFenceTriggered(closestGeofence.getRequestId(),
                            gpsManager.getLastKnownLocation().getLatitude(),
                            gpsManager.getLastKnownLocation().getLongitude());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (gpsManager.getListener() != null)
                    gpsManager.getListener().onTrigger(closestGeofence);

            }
        }
    }

    private void geoFenceTriggered(String geofence_guid, double lati, double longi) throws Exception {
        Log.i(TAG, geofence_guid);

        VisilabsGeofenceRequest request = new VisilabsGeofenceRequest(Visilabs.CallAPI().getContext());
        request.setAction("process");
        request.setApiVer("Android");
        request.setLatitude(lati);
        request.setLongitude(longi);

        String[] geofenceParts = geofence_guid.split("_");
        if (geofenceParts.length > 2) {
            request.setActionID(geofenceParts[0]);
            request.setGeofenceID(geofenceParts[2]);

            VisilabsCallback callback = new VisilabsCallback() {
                @Override
                public void success(VisilabsResponse response) {
                    String rawResponse = response.getRawResponse();
                    Log.i(TAG, rawResponse);
                }

                @Override
                public void fail(VisilabsResponse response) {
                    String rawResponse = response.getRawResponse();
                    Log.e(TAG, rawResponse);
                }
            };
            request.executeAsync(callback);
        }
    }

    private Geofence getClosestTriggeredGoefence(GpsManager gpsManager, List<Geofence> triggerList) {
        if (triggerList.size() == 0) {
            return null;
        } else if (triggerList.size() == 1) {
            return triggerList.get(0);
        } else {

            Geofence triggeredGeofence = null;
            double minDistance = Double.MAX_VALUE;
            for (Geofence geofence : triggerList) {
                for (VisilabsGeoFenceEntity geoFenceEntity : gpsManager.mActiveGeoFenceEntityList) {
                    double distance = GeoFencesUtils.haversine(gpsManager.getLastKnownLocation().getLatitude(),
                            gpsManager.getLastKnownLocation().getLongitude(),
                            Double.parseDouble(geoFenceEntity.getLatitude()), Double.parseDouble(geoFenceEntity.getLongitude()));
                    if (distance < minDistance) {
                        triggeredGeofence = geofence;
                        minDistance = distance;
                        break;
                    }
                }
            }
            return triggeredGeofence;
        }
    }
}
