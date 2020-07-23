package com.visilabs.favs;


public class FavoriteAttributeAction {
    private String actiontype;

    private String actid;

    private Actiondata actiondata;

    private String title;

    public String getActiontype() {
        return actiontype;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public String getActid() {
        return actid;
    }

    public void setActid(String actid) {
        this.actid = actid;
    }

    public Actiondata getActiondata() {
        return actiondata;
    }

    public void setActiondata(Actiondata actiondata) {
        this.actiondata = actiondata;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "FavoriteAttributeAction [actiontype = " + actiontype + ", actid = " + actid + ", actiondata = " + actiondata + ", title = " + title + "]";
    }
}

