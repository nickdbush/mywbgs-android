package com.nickdbush.mywbgs;

import android.app.Application;

import com.nickdbush.mywbgs.models.Lesson;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyWBGS extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // JodaTime
        JodaTimeAndroid.init(this);
        // Realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                // Do migrations and shit here
                .initialData(new TestData())
                .build();
        // Realm.deleteRealm(realmConfiguration);
        Realm.setDefaultConfiguration(realmConfiguration);
        // Calligraphy
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Montserrat-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    private class TestData implements Realm.Transaction {
        @Override
        public void execute(Realm realm) {
            realm.beginTransaction();
            createLesson("History", "EC2", 0, 0);
            createLesson("English", "E9", 0, 1);
            createLesson("Maths", "M4", 0, 2);
            createLesson("Computing", "T4", 0, 3);
            createLesson("French", "111", 0, 4);

            createLesson("French", "111", 1, 0);
            createLesson("English", "E9", 1, 1);
            createLesson("Chemistry", "C2", 1, 2);
            createLesson("Physics", "P3", 1, 3);
            createLesson("Maths", "M4", 1, 4);

            createLesson("English", "E9", 2, 0);
            createLesson("Computing", "T4", 2, 1);
            createLesson("History", "GS2", 2, 2);
            createLesson("Biology", "B3", 2, 3);
            createLesson("Games", "NF", 2, 4);

            createLesson("Maths", "M4", 3, 0);
            createLesson("PSHE", "EC2", 3, 1);
            createLesson("English", "E9", 3, 2);
            createLesson("Geography", "201", 3, 3);
            createLesson("Physics", "P3", 3, 4);

            createLesson("Chemistry", "C2", 4, 0);
            createLesson("Biology", "B3", 4, 1);
            createLesson("Maths", "M4", 4, 2);
            createLesson("Geography", "201", 4, 3);
            createLesson("English", "E9", 4, 4);
            realm.commitTransaction();
        }

        private void createLesson(String subject, String room, int day, int period) {
            Lesson lesson = Realm.getDefaultInstance().createObject(Lesson.class);
            lesson.setSubject(subject);
            lesson.setRoom(room);
            lesson.setDay(day);
            lesson.setPeriod(period);
        }
    }
}
