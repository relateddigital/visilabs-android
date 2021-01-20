package com.visilabs.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {
    private final Context mContext;
    private boolean mIsOnline;
    private boolean mIsListenerRegistered;
    private NetworkReceiver mReceiver;

    private static final String LOG_TAG = "com.visilabs.android.NetworkManager";

    private static NetworkManager mInstance;

    public NetworkManager(Context pContext) {
        mContext = pContext;
    }

    public void registerNetworkListener() {
        if (!mIsListenerRegistered) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            mReceiver = new NetworkReceiver();
            mContext.registerReceiver(mReceiver, filter);
            mIsListenerRegistered = true;
        }
    }

    public void unregisterNetworkListener() {
        if (mIsListenerRegistered) {
            try {
                mContext.unregisterReceiver(mReceiver);
            } catch (Exception e) {
                VisilabsLog.w(LOG_TAG, "Failed to unregister receiver");
            }
            mIsListenerRegistered = false;
        }
    }

    public void checkNetworkStatus() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        mIsOnline = networkInfo != null && networkInfo.isConnected();
        if (mIsOnline) {
            String type = networkInfo.getTypeName();
            VisilabsLog.i(LOG_TAG, "Device is online. Connection type : " + type);
        } else {
            VisilabsLog.i(LOG_TAG, "Device is offline.");
        }
    }

    public boolean isOnline() {
        return mIsOnline;
    }

    private class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkNetworkStatus();
        }
    }

    public static NetworkManager init(Context pContext) {
        if (mInstance == null) {
            mInstance = new NetworkManager(pContext);
        }
        return mInstance;
    }

    public static NetworkManager getInstance() {
        return mInstance;
    }
}
