package com.visilabs.android.util;

public class StringUtils {
    public static boolean isNullOrWhiteSpace(String value) {
        return value == null || value.trim().isEmpty();
    }
}
