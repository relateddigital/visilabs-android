package com.visilabs.gps.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VisilabsGeofenceGetListResponse {
    @SerializedName("actid")
    private Integer mActId;
    @SerializedName("dis")
    private Integer mDistance;
    @SerializedName("geo")
    private List<Geofence> mGeofences = null;
    @SerializedName("trgevt")
    private String mTrgevt;

    public Integer getActId() {
        return mActId;
    }

    public void setActId(Integer actId) {
        mActId = actId;
    }

    public Integer getDistance() {
        return mDistance;
    }

    public void setDistance(Integer distance) {
        mDistance = distance;
    }

    public List<Geofence> getGeofences() {
        return mGeofences;
    }

    public void setGeofences(List<Geofence> geofences) {
        mGeofences = geofences;
    }

    public String getTrgevt() {
        return mTrgevt;
    }

    public void setTrgevt(String trgevt) {
        mTrgevt = trgevt;
    }
}
