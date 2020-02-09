package com.visilabs.android.gps.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.visilabs.android.gps.geofence.GeofenceTransitionsIntentService;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //GeofenceTransitionsJobIntentService.enqueueWork(context, intent);
        GeofenceTransitionsIntentService.enqueueWork(context, intent);
    }
}
