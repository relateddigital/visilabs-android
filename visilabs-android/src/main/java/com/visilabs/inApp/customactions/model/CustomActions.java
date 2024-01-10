package com.visilabs.inApp.customactions.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomActions implements Serializable {

    @SerializedName("actid")
    private String actid;

    @SerializedName("title")
    private String title;

    @SerializedName("actiontype")
    private String actiontype;

    @SerializedName("actiondata")
    private CustomActionsActionData actiondata;

    @SerializedName("panelv2")
    private Boolean panelv2;

    // Getter ve Setter metotları

    public String getActid() {
        return actid;
    }

    public void setActid(String actid) {
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

    public CustomActionsActionData getActiondata() {
        return actiondata;
    }

    public void setActiondata(CustomActionsActionData actiondata) {
        this.actiondata = actiondata;
    }

    public Boolean getPanelv2() {
        return panelv2;
    }

    public void setPanelv2(Boolean panelv2) {
        this.panelv2 = panelv2;
    }
}