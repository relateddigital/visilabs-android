package com.visilabs.gps.entities;

import com.google.android.gms.location.Geofence;

public class VisilabsGeoFenceEntity {

    public String guid;
    public String lat;
    public String lng;
    public String name;
    public int radius;
    public String type;
    public double distance;
    public int geoid;

    public int durationInSeconds;

    public VisilabsGeoFenceEntity() {
    }

    public Geofence toGeofence() {
        int mTransitionType;


        switch (type) {
            case "OnEnter":
                mTransitionType = Geofence.GEOFENCE_TRANSITION_ENTER;
                break;
            case "OnExit":
                mTransitionType = Geofence.GEOFENCE_TRANSITION_EXIT;
                break;
            default:
                mTransitionType = Geofence.GEOFENCE_TRANSITION_DWELL;
                break;
        }

        if(mTransitionType == Geofence.GEOFENCE_TRANSITION_DWELL){
            return new Geofence.Builder()
                    .setRequestId(guid)
                    .setTransitionTypes(mTransitionType)
                    .setCircularRegion(Double.parseDouble(lat), Double.parseDouble(lng), radius)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setLoiteringDelay(this.durationInSeconds * 1000)
                    .build();
        }else{
            return new Geofence.Builder()
                    .setRequestId(guid)
                    .setTransitionTypes(mTransitionType)
                    .setCircularRegion(Double.parseDouble(lat), Double.parseDouble(lng), radius)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .build();
        }
    }
}