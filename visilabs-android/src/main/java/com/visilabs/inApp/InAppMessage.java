package com.visilabs.inApp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class InAppMessage implements Parcelable {

    private static final String LOG_TAG = "VisilabsNotification";

    @SerializedName("actid")
    private Integer mActId;
    @SerializedName("actiondata")
    private ActionData mActionData;
    @SerializedName("actiontype")
    private String mActionType;
    @SerializedName("title")
    private String mTitle;

    protected InAppMessage(Parcel in) {
        if (in.readByte() == 0) {
            mActId = null;
        } else {
            mActId = in.readInt();
        }
        mActionData = in.readParcelable(ActionData.class.getClassLoader());
        mActionType = in.readString();
        mTitle = in.readString();
    }

    public static final Creator<InAppMessage> CREATOR = new Creator<InAppMessage>() {
        @Override
        public InAppMessage createFromParcel(Parcel in) {
            return new InAppMessage(in);
        }

        @Override
        public InAppMessage[] newArray(int size) {
            return new InAppMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mActId);
        dest.writeParcelable(mActionData, flags);
        dest.writeString(mActionType);
        dest.writeString(mTitle);
    }

    public Integer getActId() {
        return mActId;
    }

    public void setActId(Integer actId) {
        mActId = actId;
    }

    public ActionData getActionData() {
        return mActionData;
    }

    public void setActionData(ActionData actionData) {
        mActionData = actionData;
    }

    public String getActionType() {
        return mActionType;
    }

    public void setActionType(String actionType) {
        mActionType = actionType;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
