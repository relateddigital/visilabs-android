package com.visilabs.inApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class InAppSafeUtils {
    private static final String LOG_TAG = "InAppSafeUtils";

    private static final Set<String> COLOR_NAMES = new HashSet<>(Arrays.asList(
            "red", "blue", "green", "black", "white", "gray", "cyan", "magenta", "yellow",
            "lightgray", "darkgray", "grey", "lightgrey", "darkgrey", "aqua", "fuchsia",
            "lime", "maroon", "navy", "olive", "purple", "silver", "teal"));

    private InAppSafeUtils() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Returns a string accepted by {@link Color#parseColor(String)} or null if the value is not a valid color.
     */
    public static String normalizeColor(String value) {
        if (isBlank(value)) {
            return null;
        }
        String color = value.trim();
        String hex = color.startsWith("#") ? color.substring(1) : color;
        if ((hex.length() == 6 || hex.length() == 8) && isHex(hex)) {
            return "#" + hex;
        }
        if (COLOR_NAMES.contains(color.toLowerCase(Locale.ROOT))) {
            return color.toLowerCase(Locale.ROOT);
        }
        return null;
    }

    public static boolean isValidColor(String value) {
        return normalizeColor(value) != null;
    }

    public static int parseColor(String value, int fallback) {
        String color = normalizeColor(value);
        if (color == null) {
            return fallback;
        }
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            return fallback;
        }
    }

    public static void setBackgroundColor(View view, String value) {
        if (view != null && isValidColor(value)) {
            view.setBackgroundColor(parseColor(value, Color.TRANSPARENT));
        }
    }

    public static void setTextColor(TextView textView, String value) {
        if (textView != null && isValidColor(value)) {
            textView.setTextColor(parseColor(value, textView.getCurrentTextColor()));
        }
    }

    public static float parseFloat(String value, float fallback) {
        if (isBlank(value)) {
            return fallback;
        }
        try {
            float result = Float.parseFloat(value.trim());
            return Float.isNaN(result) || Float.isInfinite(result) ? fallback : result;
        } catch (Exception e) {
            return fallback;
        }
    }

    public static int parseInt(String value, int fallback) {
        if (isBlank(value)) {
            return fallback;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            float result = parseFloat(value, Float.NaN);
            return Float.isNaN(result) ? fallback : (int) result;
        }
    }

    public static String replaceNewLines(String value) {
        return value == null ? "" : value.replace("\\n", "\n");
    }

    public static boolean openLink(Context context, Uri uri) {
        if (context == null || uri == null) {
            return false;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not open link: " + uri, e);
            return false;
        }
    }

    private static boolean isHex(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (Character.digit(value.charAt(i), 16) < 0) {
                return false;
            }
        }
        return true;
    }
}
