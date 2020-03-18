package com.visilabs.gps.geofence;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.JobIntentService;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.visilabs.Injector;
import com.visilabs.Visilabs;
import com.visilabs.VisilabsResponse;
import com.visilabs.api.VisilabsGeofenceRequest;
import com.visilabs.api.VisilabsTargetCallback;
import com.visilabs.gps.manager.GpsManager2;
import com.visilabs.json.JSONArray;

import java.util.List;

public class GeofenceTransitionsIntentService extends JobIntentService {

    private static final String TAG = "GeofenceTIService";

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, GeofenceTransitionsIntentService.class, 573, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Visilabs.CallAPI() == null) {
            Visilabs.CreateAPI(getApplicationContext());
        }
        Visilabs.CallAPI().startGpsManager();
    }

    @Override
    protected void onHandleWork(Intent intent) {
        Log.v(TAG, "onHandleWork");
        if (Visilabs.CallAPI() == null) {
            Visilabs.CreateAPI(getApplicationContext());
        }

        Visilabs.CallAPI().startGpsManager();

        final GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(intent);
        if (geoFenceEvent.hasError()) {
            int errorCode = geoFenceEvent.getErrorCode();
            Log.e(TAG, "Location Services error: " + errorCode);
            return;
        } else {
            GpsManager2 gpsManager = Injector.INSTANCE.getGpsManager2();
            if (gpsManager == null)
                return;
            List<Geofence> triggerList = geoFenceEvent.getTriggeringGeofences();
            for (final Geofence geofence : triggerList) {
                if (Looper.myLooper() == null)
                    Looper.prepare();
                Handler mainHandler = new Handler(Looper.getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            geoFenceTriggered(geofence.getRequestId(), geoFenceEvent.getGeofenceTransition());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                mainHandler.post(myRunnable);
                if (Looper.myLooper() == null)
                    Looper.loop();

            }

        }
    }

    private void geoFenceTriggered(String geofence_guid, int transition) throws Exception {
        Log.i(TAG, geofence_guid);
        //TODO: burada geofence tetiklenme requesti atılacak. alttakileri sildim.
        VisilabsGeofenceRequest request = (VisilabsGeofenceRequest) new VisilabsGeofenceRequest(Visilabs.CallAPI().getContext());
        request.setAction("process");
        request.setApiVer("Android");

        String[] geofenceParts = geofence_guid.split("_");
        if (geofenceParts != null && geofenceParts.length > 2) {
            request.setActionID(geofenceParts[0]);
            request.setGeofenceID(geofenceParts[2]);
            VisilabsTargetCallback callback = new VisilabsTargetCallback() {
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

