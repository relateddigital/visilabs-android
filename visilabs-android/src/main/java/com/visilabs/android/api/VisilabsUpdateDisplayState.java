package com.visilabs.android.api;


import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.visilabs.android.notifications.VisilabsNotification;
import com.visilabs.android.util.VisilabsConfig;

import java.util.concurrent.locks.ReentrantLock;

@TargetApi(VisilabsConfig.UI_FEATURES_MIN_API)
public class VisilabsUpdateDisplayState implements Parcelable {

    public static abstract class DisplayState implements Parcelable {

        private DisplayState() {}

        public abstract String getType();


        public static final class InAppNotificationState extends DisplayState {
            public static final String TYPE = "InAppNotificationState";

            public static final Creator<InAppNotificationState> CREATOR =
                    new Creator<InAppNotificationState>() {

                        @Override
                        public InAppNotificationState createFromParcel(final Parcel source) {
                            final Bundle read = new Bundle(InAppNotificationState.class.getClassLoader());
                            read.readFromParcel(source);
                            return new InAppNotificationState(read);
                        }

                        @Override
                        public InAppNotificationState[] newArray(final int size) {
                            return new InAppNotificationState[size];
                        }
                    };

            public InAppNotificationState(VisilabsNotification notification, int highlightColor) {
                mInAppNotification = notification;
                mHighlightColor = highlightColor;
            }

            public int getHighlightColor() {
                return mHighlightColor;
            }

            public VisilabsNotification getInAppNotification() {
                return mInAppNotification;
            }

            @Override
            public String getType() {
                return TYPE;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(final Parcel dest, final int flags) {
                final Bundle write = new Bundle();
                write.putParcelable(INAPP_KEY, mInAppNotification);
                write.putInt(HIGHLIGHT_KEY, mHighlightColor);
                dest.writeBundle(write);
            }

            private InAppNotificationState(Bundle in) {
                mInAppNotification = in.getParcelable(INAPP_KEY);
                mHighlightColor = in.getInt(HIGHLIGHT_KEY);
            }

            private final VisilabsNotification mInAppNotification;
            private final int mHighlightColor;

            private static String INAPP_KEY = "com.visilabs.android.notifications.VisilabsUpdateDisplayState.InAppNotificationState.INAPP_KEY";
            private static String HIGHLIGHT_KEY = "com.visilabs.android.notifications.VisilabsUpdateDisplayState.InAppNotificationState.HIGHLIGHT_KEY";
        }

        public static final Creator<DisplayState> CREATOR =
                new Creator<DisplayState>() {
                    @Override
                    public DisplayState createFromParcel(final Parcel source) {
                        final Bundle read = new Bundle(DisplayState.class.getClassLoader());
                        read.readFromParcel(source);
                        final String type = read.getString(STATE_TYPE_KEY);
                        final Bundle implementation = read.getBundle(STATE_IMPL_KEY);
                        if (InAppNotificationState.TYPE.equals(type)) {
                            return new InAppNotificationState(implementation);
                        }
//                        else if (SurveyState.TYPE.equals(type)) {
//                            return new SurveyState(implementation);
//                        }
                        else {
                            throw new RuntimeException("Unrecognized display state type " + type);
                        }
                    }

                    @Override
                    public DisplayState[] newArray(final int size) {
                        return new DisplayState[size];
                    }
                };

        public static final String STATE_TYPE_KEY = "com.visilabs.android.notifications.VisilabsUpdateDisplayState.InAppNotificationState.STATE_TYPE_KEY";
        public static final String STATE_IMPL_KEY = "com.visilabs.android.notifications.VisilabsUpdateDisplayState.InAppNotificationState.STATE_IMPL_KEY";
    }

    public static ReentrantLock getLockObject() {
        // Returns an unlocked lock object. Does *not* acquire a lock!
        return sUpdateDisplayLock;
    }

    public static boolean hasCurrentProposal() {
        // Almost certainly a race condition of caller doesn't hold our lock object.
        if (!sUpdateDisplayLock.isHeldByCurrentThread()) throw new AssertionError();

        final long currentTime = System.currentTimeMillis();
        final long deltaTime = currentTime - sUpdateDisplayLockMillis;

        if (sNextIntentId > 0 && deltaTime > MAX_LOCK_TIME_MILLIS) {
            Log.i(LOGTAG, "UpdateDisplayState set long, long ago, without showing. Update state will be cleared.");
            sUpdateDisplayState = null;
        }

        return null != sUpdateDisplayState;
    }

    // Returned id should either be -1, or POSITIVE (nonzero). Don't return zero, please.
    public static int proposeDisplay(final DisplayState state, final String distinctId, final String token) {
        int ret = -1;

        if (!sUpdateDisplayLock.isHeldByCurrentThread()) throw new AssertionError();
        if (! hasCurrentProposal()) {
            sUpdateDisplayLockMillis = System.currentTimeMillis();
            sUpdateDisplayState = new VisilabsUpdateDisplayState(state, distinctId, token);
            sNextIntentId++;
            ret = sNextIntentId;
        } else {
            Log.v(LOGTAG, "Already showing a Visilabs update, declining to show another.");

        }

        return ret;
    }

    /**
     * Client code should not call this method.
     */
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

    /**
     * Client code should not call this method.
     */
    public static VisilabsUpdateDisplayState claimDisplayState(final int intentId) {
        sUpdateDisplayLock.lock();
        try {
            if (sShowingIntentId > 0 && sShowingIntentId != intentId) {
                // Someone else has claimed another intent already
                return null;
            } else if (sUpdateDisplayState == null) {
                // Nothing to claim, caller is too late (or crazy)
                return null;
            } else {
                // Claim is successful
                sUpdateDisplayLockMillis = System.currentTimeMillis();
                sShowingIntentId = intentId;
                return sUpdateDisplayState;
            }
        } finally {
            sUpdateDisplayLock.unlock();
        }
    }

    public static final Parcelable.Creator<VisilabsUpdateDisplayState> CREATOR = new Parcelable.Creator<VisilabsUpdateDisplayState>() {
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

    // Package access for testing only- DO NOT CALL in library code
    VisilabsUpdateDisplayState(final DisplayState displayState, final String distinctId, final String token) {
        mDistinctId = distinctId;
        mToken = token;
        mDisplayState = displayState;
    }

    // Bundle must have the same ClassLoader that loaded this constructor.
    private VisilabsUpdateDisplayState(Bundle read) {
        mDistinctId = read.getString(DISTINCT_ID_BUNDLE_KEY);
        mToken = read.getString(TOKEN_BUNDLE_KEY);
        mDisplayState = read.getParcelable(DISPLAYSTATE_BUNDLE_KEY);
    }

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

    private static final String DISTINCT_ID_BUNDLE_KEY = "com.visilabs.android.notifications.VisilabsUpdateDisplayState.DISTINCT_ID_BUNDLE_KEY";
    private static final String TOKEN_BUNDLE_KEY = "com.visilabs.android.notifications.VisilabsUpdateDisplayState.TOKEN_BUNDLE_KEY";
    private static final String DISPLAYSTATE_BUNDLE_KEY = "com.visilabs.android.notifications.VisilabsUpdateDisplayState.DISPLAYSTATE_BUNDLE_KEY";

}

