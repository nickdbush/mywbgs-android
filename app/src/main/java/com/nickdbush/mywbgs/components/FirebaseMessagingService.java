package com.nickdbush.mywbgs.components;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.nickdbush.mywbgs.MainActivity;
import com.nickdbush.mywbgs.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public static final int NOTIFICATION_ID = 2;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Create the notification and display
        Intent notifyIntent = new Intent(getBaseContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getBaseContext())
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_silhouette)
                .build();

        // Auto-close on click
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getBaseContext());
        managerCompat.notify(NOTIFICATION_ID, notification);
    }

}