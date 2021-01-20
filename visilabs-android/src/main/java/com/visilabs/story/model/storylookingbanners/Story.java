package com.visilabs.story.model.storylookingbanners;


import java.io.Serializable;

public class Story implements Serializable {
    private String actid;

    private String title;

    private String actiontype;

    private Actiondata actiondata;

    public void setActid(String actid) {
        this.actid = actid;
    }

    public String getActid() {
        return this.actid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public String getActiontype() {
        return this.actiontype;
    }

    public void setActiondata(Actiondata Actiondata) {
        this.actiondata = Actiondata;
    }

    public Actiondata getActiondata() {
        return this.actiondata;
    }
}
