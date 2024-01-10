package com.visilabs.inApp.customactions.model;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class CustomActionsReport implements Serializable {

    @SerializedName("impression")
    private String impression;

    @SerializedName("click")
    private String click;

    // Getter ve Setter metotları

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