package com.visilabs.gps.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.visilabs.gps.geofence.GeofenceTransitionsReceiver;


public class GeoFencesUtils {
    public static PendingIntent getTransitionPendingIntent(Context context) {
        Intent intent = new Intent(context, GeofenceTransitionsReceiver.class);
        //Intent intent = new Intent("com.visilabs.gps.geofence.ACTION_RECEIVE_GEOFENCE");
        return PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static final double R = 6372.8; // In kilometers

    /**
     * Calculates the distance between two points
     * @param lat1 the latitude of the first point
     * @param lon1 the longitude of the first point
     * @param lat2 the latitude of the second point
     * @param lon2 the longitude of the second point
     * @return double - the distance in km
     */
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}