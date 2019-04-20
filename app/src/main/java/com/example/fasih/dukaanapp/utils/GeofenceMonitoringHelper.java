package com.example.fasih.dukaanapp.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.fasih.dukaanapp.home.fragments.services.GeofenceTransitionsIntentService;

public class GeofenceMonitoringHelper {

    private static PendingIntent geofencePendingIntent = null;

    public static PendingIntent getGeofencePendingIntent(Context mContext) {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(mContext, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getService(mContext, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }
}
