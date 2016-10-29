package com.nickdbush.mywbgs.models;

import org.joda.time.LocalDate;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Reminder extends RealmObject {

    private static long nextId = 0;

    @PrimaryKey
    private long id = 0;
    private String title;
    private String description;
    private int day;
    private Date date;

    public Reminder() {

    }

    public void generateId() {
        if (id == 0) {
            if (nextId == 0) {
                Number highestId = Realm.getDefaultInstance().where(Reminder.class).max("id");
                nextId = (highestId == null || highestId.longValue() == 0) ? 1 : highestId.longValue() + 1;
            }
            id = nextId++;
        }
    }

    public long getId() {
        return id;
    }

    public LocalDate getDate() {
        return new LocalDate(date);
    }

    public void setDate(LocalDate date) {
        this.date = date.toDate();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
