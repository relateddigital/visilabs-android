package com.visilabs.spinToWin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Slice implements Serializable {

    @SerializedName("displayName")
    private String displayName;
    @SerializedName("color")
    private String color;
    @SerializedName("code")
    private String code;
    @SerializedName("type")
    private String type;
    @SerializedName("is_available")
    private boolean isAvailable;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

}
