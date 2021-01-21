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

    private static final String LOG_TAG = "VisilabsUpdateDisplaySt";

    private final String mDistinctId;
    private final String mToken;
    private final DisplayState mDisplayState;

    private static final ReentrantLock mUpdateDisplayLock = new ReentrantLock();
    private static long mUpdateDisplayLockMillis = -1;
    private static VisilabsUpdateDisplayState mUpdateDisplayState = null;
    private static int mNextIntentId = 0;
    private static int mShowingIntentId = -1;

    private static final long MAX_LOCK_TIME_MILLIS = 12 * 60 * 60 * 1000; // Twelve hour timeout on notification activities

    private static final String DISTINCT_ID_BUNDLE_KEY = "VisilabmUpdateDisplayState.DISTINCT_ID_BUNDLE_KEY";
    private static final String TOKEN_BUNDLE_KEY = "VisilabmUpdateDisplayState.TOKEN_BUNDLE_KEY";
    private static final String DISPLAYSTATE_BUNDLE_KEY = "VisilabmUpdateDisplayState.DISPLAYSTATE_BUNDLE_KEY";

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

    public static ReentrantLock getLockObject() {
        return mUpdateDisplayLock;
    }

    public static boolean hasCurrentProposal() {
        if (!mUpdateDisplayLock.isHeldByCurrentThread()) throw new AssertionError();

        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - mUpdateDisplayLockMillis;

        if (mNextIntentId > 0 && deltaTime > MAX_LOCK_TIME_MILLIS) {
            Log.i(LOG_TAG, "UpdateDisplayState set long, long ago, without showing. Update state will be cleared.");
            mUpdateDisplayState = null;
        }
        return null != mUpdateDisplayState;
    }

    public static int proposeDisplay(DisplayState state, final String distinctId, final String token) {
        int ret = -1;

        if (!mUpdateDisplayLock.isHeldByCurrentThread()) throw new AssertionError();

        if (!hasCurrentProposal()) {
            mUpdateDisplayLockMillis = System.currentTimeMillis();
            mUpdateDisplayState = new VisilabsUpdateDisplayState(state, distinctId, token);
            mNextIntentId++;
            ret = mNextIntentId;
        } else {
            Log.v(LOG_TAG, "Already showing a Visilabs update, declining to show another.");
        }

        return ret;
    }

    public static void releaseDisplayState(int intentId) {
        mUpdateDisplayLock.lock();
        try {
            if (intentId == mShowingIntentId) {
                mShowingIntentId = -1;
                mUpdateDisplayState = null;
            }
        } finally {
            mUpdateDisplayLock.unlock();
        }
    }

    public static VisilabsUpdateDisplayState claimDisplayState(int intentId) {
        mUpdateDisplayLock.lock();
        try {
            if (mShowingIntentId > 0 && mShowingIntentId != intentId) {
                return null;
            } else if (mUpdateDisplayState == null) {
                return null;
            } else {
                mUpdateDisplayLockMillis = System.currentTimeMillis();
                mShowingIntentId = intentId;
                return mUpdateDisplayState;
            }
        } finally {
            mUpdateDisplayLock.unlock();
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
}

