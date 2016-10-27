package com.nickdbush.mywbgs.components;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.nickdbush.mywbgs.HomeworkActivity;
import com.nickdbush.mywbgs.R;

public class HomeworkNotificationService extends IntentService {
    public static final int NOTIFICATION_ID = 0;
    public static final int REQUEST_CODE = 0;

    public HomeworkNotificationService() {
        super("HomeworkNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent notifyIntent = new Intent(this, HomeworkActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(intent.getBundleExtra("data").getString("title", "MyWBGS"))
                .setContentText(intent.getBundleExtra("data").getString("text", ""))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_silhouette)
                .build();

        // Auto-close on click
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notification);
    }
}
