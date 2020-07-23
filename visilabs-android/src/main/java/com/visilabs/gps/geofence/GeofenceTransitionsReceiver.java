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
import com.visilabs.gps.manager.GpsManager;
import com.visilabs.json.JSONArray;

import java.util.List;

public class GeofenceTransitionsReceiver extends BroadcastReceiver {
    private static final String TAG = "GeoTransitionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Visilabs.CallAPI() == null) {
            Visilabs.CreateAPI(context.getApplicationContext());
        }

        Visilabs.CallAPI().startGpsManager();

        GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(intent);
        if (geoFenceEvent.hasError()) {
            int errorCode = geoFenceEvent.getErrorCode();
            Log.e("geoFenceEvent", String.valueOf(errorCode));
        } else {

            GpsManager gpsManager = Injector.INSTANCE.getGpsManager();

            if (gpsManager != null) {
                List<Geofence> triggerList = geoFenceEvent.getTriggeringGeofences();

                for (Geofence geofence : triggerList) {
                    try {
                        geoFenceTriggered(geofence.getRequestId(), geoFenceEvent.getGeofenceTransition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (gpsManager.getListener() != null)
                        gpsManager.getListener().onTrigger(geofence);
                }
            }
        }
    }

    private void geoFenceTriggered(String geofence_guid, int transition) throws Exception {
        Log.i(TAG, geofence_guid);
        //TODO: burada geofence tetiklenme requesti atılacak. alttakileri sildim.
        VisilabsGeofenceRequest request = new VisilabsGeofenceRequest(Visilabs.CallAPI().getContext());
        request.setAction("process");
        request.setApiVer("Android");

        String[] geofenceParts = geofence_guid.split("_");
        if (geofenceParts != null && geofenceParts.length > 2) {
            request.setActionID(geofenceParts[0]);
            request.setGeofenceID(geofenceParts[2]);

            VisilabsCallback callback = new VisilabsCallback() {
                @Override
                public void success(VisilabsResponse response) {
                    String rawResponse = response.getRawResponse();
                }

                @Override
                public void fail(VisilabsResponse response) {
                    String rawResponse = response.getRawResponse();
                    JSONArray array = response.getArray();
                }
            };
            request.executeAsync(callback);
        }

    }
}
