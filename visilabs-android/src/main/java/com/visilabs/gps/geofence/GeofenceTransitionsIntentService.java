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
import com.visilabs.api.VisilabsCallback;
import com.visilabs.gps.entities.VisilabsGeoFenceEntity;
import com.visilabs.gps.manager.GpsManager;
import com.visilabs.gps.util.GeoFencesUtils;

import java.util.List;

public class GeofenceTransitionsIntentService extends JobIntentService {

    private static final String TAG = "GeofenceTIService";

    GpsManager mGpsManager;
    List<Geofence> mTriggerList;
    GeofencingEvent mGeoFenceEvent;

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

        setGeofenceEvent(intent);
    }

    private void setGeofenceEvent(Intent intent) {

        mGeoFenceEvent = GeofencingEvent.fromIntent(intent);

        if (mGeoFenceEvent.hasError()) {
            int errorCode = mGeoFenceEvent.getErrorCode();
            Log.e(TAG, "Location Services error: " + errorCode);
        } else {
            mGpsManager = Injector.INSTANCE.getGpsManager();
            if (mGpsManager == null)
                return;

            mTriggerList = mGeoFenceEvent.getTriggeringGeofences();

            if (Looper.myLooper() == null)
                Looper.prepare();

            Handler mainHandler = new Handler(Looper.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Geofence closestGeofence = getClosestTriggeredGoefence(mGpsManager, mTriggerList);

                        if (closestGeofence != null) {
                            Log.d(TAG, "Triggered req id : " + closestGeofence.getRequestId());
                            geoFenceTriggered(closestGeofence.getRequestId(),
                                    mGpsManager.getLastKnownLocation().getLatitude(),
                                    mGpsManager.getLastKnownLocation().getLongitude());
                        }
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
                    if(response.getRawResponse().equals("ok") || response.getRawResponse().equals("\"ok\"")){
                        Log.i(TAG, "Successful Request : Sent the info of Geofence trigger");
                    } else {
                        Log.e(TAG, "Fail Request : Could not send the info of Geofence trigger");
                    }
                }

                @Override
                public void fail(VisilabsResponse response) {
                    Log.e(TAG, "Fail Request : Could not send the info of Geofence trigger");
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
                            gpsManager.getLastKnownLocation().getLongitude(), Double.parseDouble(geoFenceEntity.getLatitude()),
                            Double.parseDouble(geoFenceEntity.getLongitude()));
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