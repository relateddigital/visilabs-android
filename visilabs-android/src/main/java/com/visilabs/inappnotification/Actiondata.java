package com.visilabs.inappnotification;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Actiondata implements Serializable {
    @SerializedName("shape")
    private String shape;
    @SerializedName("pos")
    private String pos;
    @SerializedName("content_minimized_image")
    private String contentMinimizedImage;
    @SerializedName("content_minimized_text")
    private String contentMinimizedText;
    @SerializedName("content_maximized_image")
    private String contentMaximizedImage;
    @SerializedName("waiting_time")
    private String waitingTime;
    @SerializedName("ios_lnk")
    private String iosLnk;
    @SerializedName("android_lnk")
    private String androidLnk;
    @SerializedName("ExtendedProps")
    private String extendedProps;
    @SerializedName("report")
    private Report report;

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getContentMinimizedImage() {
        return contentMinimizedImage;
    }

    public void setContentMinimizedImage(String contentMinimizedImage) {
        this.contentMinimizedImage = contentMinimizedImage;
    }

    public String getContentMinimizedText() {
        return contentMinimizedText;
    }

    public void setContentMinimizedText(String contentMinimizedText) {
        this.contentMinimizedText = contentMinimizedText;
    }

    public String getContentMaximizedImage() {
        return contentMaximizedImage;
    }

    public void setContentMaximizedImage(String contentMaximizedImage) {
        this.contentMaximizedImage = contentMaximizedImage;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(String waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getIosLnk() {
        return iosLnk;
    }

    public void setIosLnk(String iosLnk) {
        this.iosLnk = iosLnk;
    }

    public String getAndroidLnk() {
        return androidLnk;
    }

    public void setAndroidLnk(String androidLnk) {
        this.androidLnk = androidLnk;
    }

    public String getExtendedProps() {
        return extendedProps;
    }

    public void setExtendedProps(String extendedProps) {
        this.extendedProps = extendedProps;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
