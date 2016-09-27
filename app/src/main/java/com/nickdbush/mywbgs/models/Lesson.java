package com.nickdbush.mywbgs.models;

import org.joda.time.LocalTime;

import io.realm.RealmObject;

public class Lesson extends RealmObject {

    // Minutes of padding used when calculating if a lesson has passed
    private static final int MINUTES_PADDING = 50;

    private int subject;
    private String room;
    private int period;
    private int day;

    public Subject getSubject() {
        return Subject.SUBJECTS[subject];
    }

    public void setSubject(Subject subject) {
        this.subject = subject.getId();
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getRawPeriod() {
        return period;
    }

    public Utils.Period getPeriod() {
        return Utils.Period.PERIODS[period];
    }

    public void setPeriod(int period) {
        if (!Utils.Period.isValid(period))
            throw new IllegalArgumentException("Period expected to be between 0 and 5");
        this.period = period;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if (day < 0 || day > 4)
            throw new IllegalArgumentException("Day expected to be between 0 and 4");
        this.day = day;
    }

    public boolean isPassed() {
        if (Utils.getCurrentSchoolDay().getDayOfWeek() - 1 != day)
            return false;
        if (new LocalTime(10, 25).plusMinutes(MINUTES_PADDING).isAfter(getPeriod().END))
            return true;
        return false;
    }

}
