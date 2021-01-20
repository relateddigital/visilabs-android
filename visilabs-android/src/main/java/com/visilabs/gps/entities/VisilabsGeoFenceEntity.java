package com.visilabs.gps.entities;

import com.google.android.gms.location.Geofence;

public class VisilabsGeoFenceEntity {

    private String mGuid;
    private String mLat;
    private String mLng;
    private String mName;
    private int mRadius;
    private String mType;
    private double mDistance;
    private int mGeoid;
    private int mDurationInSeconds;

    public VisilabsGeoFenceEntity() {
    }

    public Geofence toGeofence() {
        int mTransitionType;

        switch (mType) {
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
                    .setRequestId(mGuid)
                    .setTransitionTypes(mTransitionType)
                    .setCircularRegion(Double.parseDouble(mLat), Double.parseDouble(mLng), mRadius)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setLoiteringDelay(mDurationInSeconds * 1000)
                    .build();
        }else{
            return new Geofence.Builder()
                    .setRequestId(mGuid)
                    .setTransitionTypes(mTransitionType)
                    .setCircularRegion(Double.parseDouble(mLat), Double.parseDouble(mLng), mRadius)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .build();
        }
    }

    public String getGuid(){
        return mGuid;
    }

    public void setGuid(String guid){
        mGuid = guid;
    }

    public String getLatitude(){
        return mLat;
    }

    public void setLatitude(String latitude){
        mLat = latitude;
    }

    public String getLongitude(){
        return mLng;
    }

    public void setLongitude(String longitude){
        mLng = longitude;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public int getRadius(){
        return mRadius;
    }

    public void setRadius(int radius){
        mRadius = radius;
    }

    public String getType(){
        return mType;
    }

    public void setType(String type){
        mType = type;
    }

    public double getDistance(){
        return mDistance;
    }

    public void setDistance(double distance){
        mDistance = distance;
    }

    public int getGeoid(){
        return mGeoid;
    }

    public void setGeoid(int geoid){
        mGeoid = geoid;
    }

    public int getDurationInSeconds(){
        return mDurationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds){
        mDurationInSeconds = durationInSeconds;
    }
}