package com.visilabs.gps.geofence;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;


import com.visilabs.Injector;
import com.visilabs.Visilabs;
import com.visilabs.gps.manager.GpsManager;


public class VisilabsAlarm extends BroadcastReceiver {
    static VisilabsAlarm mAlarm = new VisilabsAlarm();

    public static VisilabsAlarm getSingleton() {
        return mAlarm;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire(10000);

        GpsManager gpsManager = Injector.INSTANCE.getGpsManager();
        if(gpsManager == null){
            if(Visilabs.CallAPI() == null){
                Visilabs.CreateAPI(context.getApplicationContext());
            }
            Visilabs.CallAPI().startGpsManager();
        }else{
            gpsManager.startGpsService();
        }
        wl.release();

    }

    public void setAlarmCheckIn(final Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, VisilabsAlarm.class);

        PendingIntent pi;

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_MUTABLE);
        } else {
            pi = PendingIntent.getBroadcast(context, 0, i, 0);
        }

        int fifteenMinutes = 15 * 60 * 1000;
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,fifteenMinutes, pi);
    }
}
