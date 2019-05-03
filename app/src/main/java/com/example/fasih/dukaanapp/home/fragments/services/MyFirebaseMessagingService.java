package com.example.fasih.dukaanapp.home.fragments.services;

import android.content.Intent;

import com.example.fasih.dukaanapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Intent myBroadcastServiceIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        myBroadcastServiceIntent = new Intent();
        myBroadcastServiceIntent.setAction("com.example.fasih.dukaanapp_FCM_REGISTRATION_TOKEN");
//        myBroadcastServiceIntent.setPackage(getPackageName());
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        //broadcast the user/shop to update the token in the server
        myBroadcastServiceIntent.putExtra(getString(R.string.fcm_token), token);
        sendBroadcast(myBroadcastServiceIntent);
    }
}
