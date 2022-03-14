package com.visilabs.util;

import android.app.Activity;

public class ActivityUtils {
    private static Activity mParentActivity;

    public static Activity getParentActivity() {
        return mParentActivity;
    }

    public static void setParentActivity(Activity parentActivity) {
        mParentActivity = parentActivity;
    }
}
