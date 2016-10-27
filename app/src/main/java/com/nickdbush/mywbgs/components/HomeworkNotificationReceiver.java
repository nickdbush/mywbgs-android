package com.nickdbush.mywbgs.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nickdbush.mywbgs.models.Homework;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeworkNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int dayOfWeek = new LocalDate().getDayOfWeek();
        // Don't want notifications of Friday or Saturday
        if (dayOfWeek == 5 || dayOfWeek == 6) return;

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Homework> homeworks = realm.where(Homework.class)
                .equalTo("dueDate", new LocalDate().plusDays(1).toDate())
                .equalTo("completed", false)
                .findAll();

        String title;
        String text = "";
        List<String> subjects = new ArrayList<String>();

        int count = 0;
        if (homeworks.size() > 0) {
            for (int i = 0; i < homeworks.size(); i++) {
                Homework homework = homeworks.get(i);
                count++;
                if (subjects.contains(homework.getLesson().getSubject())) continue;
                subjects.add(homework.getLesson().getSubject());
            }
        } else {
            return;
        }

        for (int i = 0; i < subjects.size(); i++) {
            if (i + i < subjects.size())
                text += subjects.get(i) + (i + 2 < subjects.size() ? ", " : " ");
            else
                text += "and " + subjects.get(i);
        }

        title = count + " piece" + (count > 1 ? "s" : "") + " of homework need to be done for tomorrow";

        Intent notificationIntent = new Intent(context, HomeworkNotificationService.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("text", text);
        notificationIntent.putExtra("data", bundle);
        context.startService(notificationIntent);
    }
}
