package com.nickdbush.mywbgs.models;

import org.joda.time.LocalDate;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Homework extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private String description;
    private Date dueDate;
    private int period;
    private boolean completed;

    public Homework() {

    }

    public int getId() {
        return id;
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

    public LocalDate getDueDate() {
        return new LocalDate(dueDate);
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate.toDate();
    }

    public int getRawPeriod() {
        return period;
    }

    public Utils.Period getPeriod() {
        return Utils.Period.PERIODS[getRawPeriod()];
    }

    public void setPeriod(int period) {
        if (!Utils.Period.isValid(period))
            throw new IllegalArgumentException("Period expected to be between 0 and 5");
        this.period = period;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Lesson getLesson() {
        return Realm.getDefaultInstance().where(Lesson.class)
                .equalTo("day", getDueDate().getDayOfWeek() - 1)
                .equalTo("period", getRawPeriod())
                .findFirst();
    }

    public void setLesson(Lesson lesson) {
        setDueDate(getDueDate().withDayOfWeek(lesson.getDay()));
        setPeriod(lesson.getRawPeriod());
    }

}
