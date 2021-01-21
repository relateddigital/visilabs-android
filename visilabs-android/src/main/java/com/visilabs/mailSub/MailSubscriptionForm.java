package com.visilabs.mailSub;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class MailSubscriptionForm implements Serializable, Parcelable {
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

    public MailSubscriptionForm(Parcel in) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<MailSubscriptionForm> CREATOR = new Parcelable.Creator<MailSubscriptionForm>() {

        @Override
        public MailSubscriptionForm createFromParcel(Parcel source) {
            return new MailSubscriptionForm(source);
        }

        @Override
        public MailSubscriptionForm[] newArray(int size) {
            return new MailSubscriptionForm[size];
        }
    };
}
