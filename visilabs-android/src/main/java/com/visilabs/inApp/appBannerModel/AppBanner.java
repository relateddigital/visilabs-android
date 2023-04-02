package com.visilabs.inApp.appBannerModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppBanner implements Serializable {
    @SerializedName("actid")
    private Integer actId;

    @SerializedName("title")
    private String title;

    @SerializedName("actiontype")
    private String actionType;

    @SerializedName("actiondata")
    private AppBannerActionData actionData;

    // getter and setter methods
    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public AppBannerActionData getActionData() {
        return actionData;
    }

    public void setActionData(AppBannerActionData actionData) {
        this.actionData = actionData;
    }
}
