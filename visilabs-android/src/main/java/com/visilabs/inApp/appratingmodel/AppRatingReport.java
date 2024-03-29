package com.visilabs.inApp.appratingmodel;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class AppRatingReport {

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
