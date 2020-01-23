package com.visilabs.android.util;

import android.content.Context;
import com.visilabs.android.util.VisilabsLog;

public class Device {

    private static String mDeviceId;
    private static String mDeviceName;
    private static String mUserAgent;

    private static final String MANUFACTURER_FIELD = "MANUFACTURER";
    private static final String USER_AGENT_TEMP = "Android %s; %s";

    private static final String LOG_TAG = "VisilabsAPI.Device";

    /**
     * Gets the unique device ID.
     *
     * @param context Application context
     * @return the unique device id
     */
    public static String getDeviceId(Context context) {
        if (mDeviceId == null) {
            mDeviceId = android.provider.Settings.Secure.getString(
                    context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
        }
        return mDeviceId;
    }

    /**
     * Gets the model and manufacturer (if possible) of the device.
     *
     * @return the model and manufacturer (if found) of the device
     */
    public static String getDeviceModelAndManufacturer() {
        if (mDeviceName == null) {
            String model = android.os.Build.MODEL;
            String deviceName = model;
            String manufacturer = android.os.Build.MANUFACTURER;
            if (manufacturer != null && !manufacturer.isEmpty()) {
                deviceName = manufacturer + ' ' + model;
            } else {
                VisilabsLog.w(LOG_TAG, "Could not retrieve device manufacturer info");
            }
            mDeviceName = deviceName;
        }
        return mDeviceName;
    }

    /**
     * Gets the custom user agent string.
     *
     * @return the custom user agent string
     */
    public static String getUserAgent() {
        if (mUserAgent == null) {
            mUserAgent = String.format(USER_AGENT_TEMP, android.os.Build.VERSION.RELEASE, getDeviceModelAndManufacturer());
            VisilabsLog.d(LOG_TAG, "UA = " + mUserAgent);
        }
        return mUserAgent;
    }
}
