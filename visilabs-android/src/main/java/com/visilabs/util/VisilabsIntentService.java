package com.visilabs.util;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by visilabs on 13.07.2016.
 */
public class VisilabsIntentService extends IntentService {
    public static final String TAG = "TxtSaver";
    public String name = "";

    public VisilabsIntentService(String name) {
        super(name);
        this.name = name;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }
}
