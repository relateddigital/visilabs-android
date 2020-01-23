package com.visilabs.android.gps.geofence;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.gson.Gson;
import com.visilabs.android.Visilabs;
import com.visilabs.android.VisilabsResponse;
import com.visilabs.android.api.VisilabsGeofenceRequest;
import com.visilabs.android.api.VisilabsTargetCallback;
import com.visilabs.android.Injector;
import com.visilabs.android.gps.manager.GpsManager;
import com.visilabs.android.json.JSONArray;

import java.util.List;

/**
 * Created by visilabs on 27.07.2016.
 */
public class GeofenceIntentService extends IntentService {
    private final String TAG = GeofenceIntentService.class.getName();
    private SharedPreferences prefs;
    private Gson gson;

    public GeofenceIntentService() {
        super("GeofenceIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Visilabs.CallAPI().startGpsManager();
        //TODO: Injector yerine kendim çalıştırdım.


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
        if(geofenceParts != null && geofenceParts.length > 0){
            request.setActionID(geofenceParts[0]);
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




        //SendGeofenceEventRequest startGeofenceEvent = new SendGeofenceEventRequest(geofence_guid, transition);
        //startGeofenceEvent.startHttpRequest();
        //Log.e(TAG, "geoFenceTriggered");
    }

}
