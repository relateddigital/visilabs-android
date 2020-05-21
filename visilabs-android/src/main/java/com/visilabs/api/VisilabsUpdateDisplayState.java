package com.visilabs.api;


import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.visilabs.DisplayState;
import com.visilabs.util.VisilabsConstant;

import java.util.concurrent.locks.ReentrantLock;

@TargetApi(VisilabsConstant.UI_FEATURES_MIN_API)
public class VisilabsUpdateDisplayState implements Parcelable {


    private final String mDistinctId;
    private final String mToken;
    private final DisplayState mDisplayState;

    private static final ReentrantLock sUpdateDisplayLock = new ReentrantLock();
    private static long sUpdateDisplayLockMillis = -1;
    private static VisilabsUpdateDisplayState sUpdateDisplayState = null;
    private static int sNextIntentId = 0;
    private static int sShowingIntentId = -1;

    private static final String LOGTAG = "VisilabsUpdateDisplaySt";
    private static final long MAX_LOCK_TIME_MILLIS = 12 * 60 * 60 * 1000; // Twelve hour timeout on notification activities

    private static final String DISTINCT_ID_BUNDLE_KEY = "VisilabsUpdateDisplayState.DISTINCT_ID_BUNDLE_KEY";
    private static final String TOKEN_BUNDLE_KEY = "VisilabsUpdateDisplayState.TOKEN_BUNDLE_KEY";
    private static final String DISPLAYSTATE_BUNDLE_KEY = "VisilabsUpdateDisplayState.DISPLAYSTATE_BUNDLE_KEY";

    public static ReentrantLock getLockObject() {
        return sUpdateDisplayLock;
    }

    public static boolean hasCurrentProposal() {
        if (!sUpdateDisplayLock.isHeldByCurrentThread()) throw new AssertionError();

        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - sUpdateDisplayLockMillis;

        if (sNextIntentId > 0 && deltaTime > MAX_LOCK_TIME_MILLIS) {
            Log.i(LOGTAG, "UpdateDisplayState set long, long ago, without showing. Update state will be cleared.");
            sUpdateDisplayState = null;
        }
        return null != sUpdateDisplayState;
    }

    public static int proposeDisplay(DisplayState state, final String distinctId, final String token) {
        int ret = -1;

        if (!sUpdateDisplayLock.isHeldByCurrentThread()) throw new AssertionError();

        if (!hasCurrentProposal()) {
            sUpdateDisplayLockMillis = System.currentTimeMillis();
            sUpdateDisplayState = new VisilabsUpdateDisplayState(state, distinctId, token);
            sNextIntentId++;
            ret = sNextIntentId;
        } else {
            Log.v(LOGTAG, "Already showing a Visilabs update, declining to show another.");
        }

        return ret;
    }

    public static void releaseDisplayState(int intentId) {
        sUpdateDisplayLock.lock();
        try {
            if (intentId == sShowingIntentId) {
                sShowingIntentId = -1;
                sUpdateDisplayState = null;
            }
        } finally {
            sUpdateDisplayLock.unlock();
        }
    }

    public static VisilabsUpdateDisplayState claimDisplayState(int intentId) {
        sUpdateDisplayLock.lock();
        try {
            if (sShowingIntentId > 0 && sShowingIntentId != intentId) {
                return null;
            } else if (sUpdateDisplayState == null) {
                return null;
            } else {
                sUpdateDisplayLockMillis = System.currentTimeMillis();
                sShowingIntentId = intentId;
                return sUpdateDisplayState;
            }
        } finally {
            sUpdateDisplayLock.unlock();
        }
    }

    public static Parcelable.Creator<VisilabsUpdateDisplayState> CREATOR = new Parcelable.Creator<VisilabsUpdateDisplayState>() {
        @Override
        public VisilabsUpdateDisplayState createFromParcel(Parcel in) {
            final Bundle read = new Bundle(VisilabsUpdateDisplayState.class.getClassLoader());
            read.readFromParcel(in);
            return new VisilabsUpdateDisplayState(read);
        }

        @Override
        public VisilabsUpdateDisplayState[] newArray(int size) {
            return new VisilabsUpdateDisplayState[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        final Bundle bundle = new Bundle();
        bundle.putString(DISTINCT_ID_BUNDLE_KEY, mDistinctId);
        bundle.putString(TOKEN_BUNDLE_KEY, mToken);
        bundle.putParcelable(DISPLAYSTATE_BUNDLE_KEY, mDisplayState);
        dest.writeBundle(bundle);
    }

    public DisplayState getDisplayState() {
        return mDisplayState;
    }

    public String getDistinctId() {
        return mDistinctId;
    }

    public String getToken() {
        return mToken;
    }

    private VisilabsUpdateDisplayState(DisplayState displayState, String distinctId, String token) {
        mDistinctId = distinctId;
        mToken = token;
        mDisplayState = displayState;
    }

    private VisilabsUpdateDisplayState(Bundle read) {
        mDistinctId = read.getString(DISTINCT_ID_BUNDLE_KEY);
        mToken = read.getString(TOKEN_BUNDLE_KEY);
        mDisplayState = read.getParcelable(DISPLAYSTATE_BUNDLE_KEY);
    }
}

