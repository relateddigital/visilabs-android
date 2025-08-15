package com.visilabs.survey.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SurveyModel implements Serializable {

    public ArrayList<String> fontFiles = new ArrayList<>();

    @SerializedName("actid")
    private Integer actid;
    @SerializedName("title")
    private String title;
    @SerializedName("actiontype")
    private String actiontype;
    @SerializedName("actiondata")
    private Actiondata actiondata;

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

    public Actiondata getActiondata() {
        return actiondata;
    }

    public void setActiondata(Actiondata actiondata) {
        this.actiondata = actiondata;
    }

}
