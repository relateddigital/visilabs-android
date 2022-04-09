package com.visilabs.inappnotification;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Report implements Serializable {
    @SerializedName("impression")
    private String impression;
    @SerializedName("click")
    private String click;

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }
}
