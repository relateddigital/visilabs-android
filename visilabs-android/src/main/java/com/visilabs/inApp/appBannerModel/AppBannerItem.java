package com.visilabs.inApp.appBannerModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppBannerItem implements Serializable {

    @SerializedName("img")
    private String image;

    @SerializedName("ios_lnk")
    private String iosLink;

    @SerializedName("android_lnk")
    private String androidLink;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIosLink() {
        return iosLink;
    }

    public void setIosLink(String iosLink) {
        this.iosLink = iosLink;
    }

    public String getAndroidLink() {
        return androidLink;
    }

    public void setAndroidLink(String androidLink) {
        this.androidLink = androidLink;
    }
}
