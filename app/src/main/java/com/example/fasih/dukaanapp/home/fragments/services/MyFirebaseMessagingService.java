package com.example.fasih.dukaanapp.home.fragments.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.fasih.dukaanapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Intent myBroadcastServiceIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        myBroadcastServiceIntent = new Intent();
        myBroadcastServiceIntent.setAction("com.example.fasih.dukaanapp_FCM_REGISTRATION_TOKEN");
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        myBroadcastServiceIntent.putExtra(getString(R.string.fcm_token), token);
        sendBroadcast(myBroadcastServiceIntent);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null)
            createForgroundNotification(remoteMessage.getNotification());
    }

    private void createForgroundNotification(RemoteMessage.Notification notification) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
                .setSmallIcon(R.drawable.logo_shopping)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //do this only for api 26 or greater
        createNotificationChannel();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) (Math.random() * 1000), builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID)
                    , getString(R.string.channel_name)
                    , NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription(getString(R.string.channel_description));
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
    }
}
