package com.visilabs.android.gps.entities;

import com.google.android.gms.location.Geofence;

public class VisilabsGeoFenceEntity {

    public String guid;
    public String lat;
    public String lng;
    public String name;
    public int radius;
    public String type;
    public double distance;

    public int durationInSeconds;

    public VisilabsGeoFenceEntity() {
    }

    public VisilabsGeoFenceEntity(String guid, String lat, String lng, String name, int radius, String type, double distance, int durationInSeconds) {
        this.guid = guid;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.radius = radius;
        this.type = type;
        this.distance = distance;
        this.durationInSeconds = durationInSeconds;
    }

    public Geofence toGeofence() {
        int mTransitionType;
        //mTransitionType = Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT;
        /*switch (type) {
            case "enter":
                mTransitionType = Geofence.GEOFENCE_TRANSITION_ENTER;
                break;
            case "exit":
                mTransitionType = Geofence.GEOFENCE_TRANSITION_EXIT;
                break;
            default:
                mTransitionType = Geofence.GEOFENCE_TRANSITION_DWELL;
                break;
        }*/

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