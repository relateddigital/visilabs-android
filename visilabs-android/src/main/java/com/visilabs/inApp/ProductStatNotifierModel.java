package com.visilabs.inApp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductStatNotifierModel implements Serializable {

    @SerializedName("actid")
    private Integer actid;
    @SerializedName("title")
    private String title;
    @SerializedName("actiontype")
    private String actiontype;
    @SerializedName("actiondata")
    private ProductStatNotifierActionData actiondata;

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

    public ProductStatNotifierActionData getActiondata() {
        return actiondata;
    }

    public void setActiondata(ProductStatNotifierActionData actiondata) {
        this.actiondata = actiondata;
    }

}
