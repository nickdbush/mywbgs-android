package com.nickdbush.mywbgs.components;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.nickdbush.mywbgs.HomeworkActivity;
import com.nickdbush.mywbgs.R;
import com.nickdbush.mywbgs.models.Homework;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeworkNotificationManager extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 1;
    public final static int HOMEWORK_ALARM_ID = 0;
    public final static DateTime NOTIFICATION_TIME = new LocalDate().toDateTime(new LocalTime(15, 20));

    @Override
    public void onReceive(Context context, Intent intent) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Homework> homeworks = realm.where(Homework.class)
                .lessThanOrEqualTo("dueDate", new LocalDate().plusDays(1).toDate())
                .equalTo("completed", false)
                .findAll()
                .sort("dueDate");

        String title;
        String text = "";
        List<String> subjects = new ArrayList<String>();

        int count = 0;
        if (homeworks.size() > 0) {
            for (int i = 0; i < homeworks.size(); i++) {
                Homework homework = homeworks.get(i);
                count++;
                if (subjects.contains(homework.getLesson().getSubject())) continue;
                boolean isOverdue = homeworks.get(i).getDueDate().toLocalDateTime(new LocalTime(8, 30)).isBefore(new LocalDateTime());
                subjects.add(homework.getLesson().getSubject() + (isOverdue ? " (overdue)" : ""));
            }
        } else {
            return;
        }

        for (int i = 0; i < subjects.size(); i++) {
            if (i + 1 < subjects.size())
                text += subjects.get(i) + (i + 2 < subjects.size() ? ", " : " ");
            else
                text += "and " + subjects.get(i);
        }

        title = count + " piece" + (count > 1 ? "s" : "") + " of homework " + (count > 1 ? "need" : "needs") + " to be done for tomorrow";

        // Create the notification and display
        Intent notifyIntent = new Intent(context, HomeworkActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_silhouette)
                .build();

        // Auto-close on click
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(NOTIFICATION_ID, notification);
        realm.close();
    }

    public static void setEnabled(Context context, boolean enabled) {
        Intent notifyIntent = new Intent(context, HomeworkNotificationManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, HOMEWORK_ALARM_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (enabled) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, NOTIFICATION_TIME.getMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
        }
    }
}
