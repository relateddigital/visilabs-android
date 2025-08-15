package com.visilabs.notificationbell.model;

import com.google.gson.annotations.SerializedName;

public class NotificationBellTexts {

    @SerializedName("text")
    private String text;

    @SerializedName("ios_lnk")
    private String ios_lnk;

    @SerializedName("android_lnk")
    private String android_lnk;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIos_lnk() {
        return ios_lnk;
    }

    public void setIos_lnk(String ios_lnk) {
        this.ios_lnk = ios_lnk;
    }

    public String getAndroid_lnk() {
        return android_lnk;
    }

    public void setAndroid_lnk(String android_lnk) {
        this.android_lnk = android_lnk;
    }
}