package com.visilabs.countdownTimerBanner.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CountdownTimerBanner implements Serializable {

    @SerializedName("actid")
    private Integer actid;

    @SerializedName("title")
    private String title;

    @SerializedName("actiontype")
    private String actiontype;

    @SerializedName("actiondata")
    private CountdownTimerBannerActionData actiondata;

    public Integer getActid() {
        return actid;
    }

    public void setActid(Integer actid) {
        this.actid = actid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActiontype() {
        return actiontype;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public CountdownTimerBannerActionData getActiondata() {
        return actiondata;
    }

    public void setActiondata(CountdownTimerBannerActionData actiondata) {
        this.actiondata = actiondata;
    }

}