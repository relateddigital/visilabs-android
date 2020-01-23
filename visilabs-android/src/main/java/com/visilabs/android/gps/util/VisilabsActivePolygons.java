package com.visilabs.android.gps.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisilabsActivePolygons {

    @SerializedName("guid")
    @Expose
    public String guid;
    @SerializedName("time")
    @Expose
    public int time;

    public VisilabsActivePolygons(String guid, int time) {
        this.guid = guid;
        this.time = time;
    }
}
