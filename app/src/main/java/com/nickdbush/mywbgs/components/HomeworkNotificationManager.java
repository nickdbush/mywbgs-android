package com.nickdbush.mywbgs.components;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

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
import io.realm.Sort;

public class HomeworkNotificationManager extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 1;
    public final static int HOMEWORK_ALARM_ID = 0;

    public static void setEnabled(Context context, boolean enabled, boolean showToday) {
        Intent notifyIntent = new Intent(context, HomeworkNotificationManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, HOMEWORK_ALARM_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (enabled) {
            DateTime notificationTime = new LocalDate().toDateTime(new LocalTime(15, 20));
            if (!showToday && notificationTime.isBefore(new DateTime())) {
                notificationTime = notificationTime.plusDays(1);
            }
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, notificationTime.getMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, HomeworkNotificationService.class);
        context.startService(intentService);
    }

    public static class HomeworkNotificationService extends IntentService {
        public HomeworkNotificationService() {
            super("HomeworkNotificationManager");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            Realm realm = Realm.getDefaultInstance();
            try {
                RealmResults<Homework> homeworks = realm.where(Homework.class)
                        .lessThanOrEqualTo("dueDate", new LocalDate().plusDays(1).toDate())
                        .equalTo("completed", false)
                        .findAll()
                        // TODO: 30/10/2016 Test this
                        .sort("dueDate", Sort.ASCENDING, "period", Sort.ASCENDING);

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
                    if (i + 1 < subjects.size()) {
                        text += subjects.get(i) + (i + 2 < subjects.size() ? ", " : " ");
                    } else if (subjects.size() > 1) {
                        text += "and " + subjects.get(i);
                    } else {
                        text = subjects.get(i);
                    }
                }

                title = count + " piece" + (count > 1 ? "s" : "") + " of homework " + (count > 1 ? "need" : "needs") + " to be done for tomorrow";

                // Create the notification and display
                Intent notifyIntent = new Intent(this, HomeworkActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification notification = new Notification.Builder(this)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_silhouette)
                        .build();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notification.color = ContextCompat.getColor(this, R.color.colorPrimaryDark);
                }

                // Auto-close on click
                notification.flags |= Notification.FLAG_AUTO_CANCEL;

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
                managerCompat.notify(NOTIFICATION_ID, notification);
            } finally {
                realm.close();
                stopSelf();
            }
        }
    }

}
