package com.visilabs.util;

import android.net.Uri;
import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
    private static Pattern mPattern;
    private static Matcher mMatcher;

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

    public static String[] splitRGBA(String rgba) {
        String parenthesis = rgba.replace("rgba", "");
        String test = parenthesis.replaceAll("[\\[\\](){}]", "");

        return test.split(",");
    }

    public static boolean validateHexColor(final String hexColorCode) {
        mPattern = Pattern.compile(HEX_PATTERN);

        mMatcher = mPattern.matcher(hexColorCode);
        return mMatcher.matches();
    }
}
