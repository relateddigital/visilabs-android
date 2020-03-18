package com.visilabs.util;

import android.net.Uri;
import android.util.Log;

public class StringUtils {
    public static boolean isNullOrWhiteSpace(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static Uri getURIfromUrlString(String uriString) {

        Uri uri = null;
        try {
            uri = Uri.parse(uriString);
        } catch (final IllegalArgumentException e) {
            Log.i("Visilabs", "Can't parse notification URI, will not take any action", e);
        }

        return uri;
    }
}
