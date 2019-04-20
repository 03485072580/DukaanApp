package com.example.fasih.dukaanapp.home.fragments.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class GeofenceTransitionsIntentService extends IntentService {


    public GeofenceTransitionsIntentService(){
        super(null);
    }

    public GeofenceTransitionsIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
