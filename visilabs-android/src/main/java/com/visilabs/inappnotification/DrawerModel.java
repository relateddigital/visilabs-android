package com.visilabs.inappnotification;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DrawerModel implements Serializable {
    @SerializedName("actid")
    private String actId;
    @SerializedName("title")
    private String title;
    @SerializedName("actiontype")
    private String actionType;
    @SerializedName("actiondata")
    private Actiondata actionData;

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
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

    public Actiondata getActionData() {
        return actionData;
    }

    public void setActionData(Actiondata actionData) {
        this.actionData = actionData;
    }
}
