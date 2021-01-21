package com.visilabs.util;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    /**
     * Called to save supplied value in shared preferences against given key.
     * @param context Context of caller activity
     * @param key Key of value to save against
     * @param value Value to save
     */
    public static void saveToPrefs(Context context, String name, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.apply();
    }

    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     * @param context Context of caller activity
     * @param key Key to find value against
     * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    public static String getFromPrefs(Context context, String name, String key, String defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
        try {
            return prefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
    /**
     *
     * @param context Context of caller activity
     * @param key Key to delete from SharedPreferences
     */
    public static void removeFromPrefs(Context context, String name, String key) {
        SharedPreferences prefs = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }
}