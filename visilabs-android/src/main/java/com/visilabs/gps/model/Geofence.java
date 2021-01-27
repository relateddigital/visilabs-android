package com.visilabs.gps.model;

import com.google.gson.annotations.SerializedName;

public class Geofence {
    @SerializedName("id")
    private Integer mId;
    @SerializedName("lat")
    private Double mLatitude;
    @SerializedName("long")
    private Double mLongitude;
    @SerializedName("rds")
    private Double mRadius;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    public Double getRadius() {
        return mRadius;
    }

    public void setRadius(Double radius) {
        mRadius = radius;
    }
}
