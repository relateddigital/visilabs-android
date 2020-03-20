package com.visilabs;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public abstract class DisplayState implements Parcelable {

    public abstract String getType();

    private static final String STATE_TYPE_KEY = "STATE_TYPE_KEY";
    private static final String STATE_IMPL_KEY = "STATE_IMPL_KEY";

    public static final Creator<DisplayState> CREATOR =
            new Creator<DisplayState>() {
                @Override
                public DisplayState createFromParcel(final Parcel source) {
                    Bundle read = new Bundle(DisplayState.class.getClassLoader());
                    read.readFromParcel(source);
                    String type = read.getString(STATE_TYPE_KEY);
                    Bundle implementation = read.getBundle(STATE_IMPL_KEY);
                    if (InAppNotificationState.TYPE.equals(type)) {
                        return new InAppNotificationState(implementation);
                    } else {
                        throw new RuntimeException("Unrecognized display state type " + type);
                    }
                }
                @Override
                public DisplayState[] newArray(int size) {
                    return new DisplayState[size];
                }
            };
}
