package com.visilabs.util;

import android.net.Uri;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
    private static Pattern pattern;
    private static Matcher matcher;

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
        String paranthesis = rgba.replace("rgba", "");
        String test = paranthesis.replaceAll("[\\[\\](){}]", "");
        String result[] = test.split(",");

        return result;
    }

    public static boolean validateHexColor(final String hexColorCode) {
        pattern = Pattern.compile(HEX_PATTERN);

        matcher = pattern.matcher(hexColorCode);
        return matcher.matches();
    }
}
