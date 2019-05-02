package com.example.fasih.dukaanapp.home.fragments.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        //broadcast the user to update the token in the server
        Log.d("TAG1234", "onNewToken: " + token);
        sendTokenToServer(token);
    }

    private void sendTokenToServer(String token) {

    }
}
