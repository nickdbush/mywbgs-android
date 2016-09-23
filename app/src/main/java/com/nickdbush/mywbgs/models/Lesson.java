package com.nickdbush.mywbgs.models;

import io.realm.RealmObject;

public class Lesson extends RealmObject {
    private String subject;
    private String room;
    private int period;
    private int day;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
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
        if(!Utils.Period.isValid(period))
            throw new IllegalArgumentException("Period expected to be between 0 and 5");
        this.period = period;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if(day < 0 || day > 4)
            throw new IllegalArgumentException("Day expected to be between 0 and 4");
        this.day = day;
    }
}
