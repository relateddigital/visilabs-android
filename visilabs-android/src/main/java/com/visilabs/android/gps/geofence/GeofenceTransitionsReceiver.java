package com.visilabs.android.gps.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.List;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.visilabs.android.Visilabs;
import com.visilabs.android.VisilabsResponse;
import com.visilabs.android.api.VisilabsGeofenceRequest;
import com.visilabs.android.api.VisilabsTargetCallback;
import com.visilabs.android.Injector;
import com.visilabs.android.gps.manager.GpsManager;
import com.visilabs.android.json.JSONArray;

public class GeofenceTransitionsReceiver extends BroadcastReceiver {
    private static final String TAG = "GeoTransitionReceiver";
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Visilabs.CallAPI() == null){
            Visilabs.CreateAPI(context.getApplicationContext());
        }

        Visilabs.CallAPI().startGpsManager();
        //TODO: Injector yerine kendim çalıştırdım.

        this.context = context;
        GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(intent);
        if (geoFenceEvent.hasError()) {
            int errorCode = geoFenceEvent.getErrorCode();
        } else {
            GpsManager gpsManager = Injector.INSTANCE.getGpsManager();
            if (gpsManager == null)
                return;
            List<Geofence> triggerList = geoFenceEvent.getTriggeringGeofences();
            for (Geofence geofence : triggerList) {
                geoFenceTriggered(geofence.getRequestId(), geoFenceEvent.getGeofenceTransition());
                if (gpsManager.getListener() != null)
                    gpsManager.getListener().onTrigger(geofence);
            }
        }
    }

    private void geoFenceTriggered(String geofence_guid, int transition) {
        Log.i(TAG, geofence_guid);
        //TODO: burada geofence tetiklenme requesti atılacak. alttakileri sildim.
        VisilabsGeofenceRequest request = (VisilabsGeofenceRequest) new VisilabsGeofenceRequest(Visilabs.CallAPI().getContext());
        request.setAction("process");
        request.setApiVer("Android");

        String[] geofenceParts = geofence_guid.split("_");
        if(geofenceParts != null && geofenceParts.length > 2){
            request.setActionID(geofenceParts[0]);
            request.setGeofenceID(geofenceParts[2]);

            VisilabsTargetCallback callback = new VisilabsTargetCallback() {
                @Override
                public void success(VisilabsResponse response) {
                    String rawResponse = response.getRawResponse();
                    try{

                    }catch (Exception ex){
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

    }
}
