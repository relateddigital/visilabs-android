package com.visilabs.countdownTimerBanner.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CountdownTimerBannerActionData implements Serializable {

    @SerializedName("scratch_color")
    private String scratch_color;

    @SerializedName("waiting_time")
    private Integer waiting_time;

    @SerializedName("ExtendedProps")
    private String extendedProps;

    @SerializedName("report")
    private CountdownTimerBannerReport report;

    @SerializedName("android_lnk")
    private String android_lnk;

    @SerializedName("img")
    private String img;

    @SerializedName("content_body")
    private String content_body;

    @SerializedName("counter_Date")
    private String counter_Date;

    @SerializedName("counter_Time")
    private String counter_Time;


    public String getScratch_color() {
        return scratch_color;
    }

    public void setScratch_color(String scratch_color) {
        this.scratch_color = scratch_color;
    }

    public Integer getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(Integer waiting_time) {
        this.waiting_time = waiting_time;
    }


    public String getExtendedProps() {
        return extendedProps;
    }

    public void setExtendedProps(String extendedProps) {
        this.extendedProps = extendedProps;
    }

    public CountdownTimerBannerReport getReport() {
        return report;
    }

    public void setReport(CountdownTimerBannerReport report) {
        this.report = report;
    }


    public String getAndroid_lnk() {
        return android_lnk;
    }

    public void setAndroid_lnk(String android_lnk) {
        this.android_lnk = android_lnk;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent_body() {
        return content_body;
    }

    public void setContent_body(String content_body) {
        this.content_body = content_body;
    }

    public String getCounter_Date() {
        return counter_Date;
    }

    public void setCounter_Date(String counter_Date) {
        this.counter_Date = counter_Date;
    }

    public String getCounter_Time() {
        return counter_Time;
    }

    public void setCounter_Time(String counter_Time) {
        this.counter_Time = counter_Time;
    }
}