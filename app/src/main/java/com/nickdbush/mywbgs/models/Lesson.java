package com.nickdbush.mywbgs.models;

import org.joda.time.LocalDateTime;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Lesson extends RealmObject {
    // Minutes of padding used when calculating if a lesson has passed
    private static final int MINUTES_PADDING = 50;

    private static long nextId = 0;

    @PrimaryKey
    private long id = 0;
    private String subject;
    private String room;
    private int period;
    private int day;

    public Lesson() {

    }

    public void generateId() {
        if (id == 0) {
            if (nextId == 0) {
                Number highestId = Realm.getDefaultInstance().where(Lesson.class).max("id");
                nextId = (highestId == null || highestId.longValue() == 0) ? 1 : highestId.longValue() + 1;
            }
            id = nextId++;
        }
    }

    public long getId() {
        return id;
    }

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
        // Does this increment the day as well? If not, that's the cause of our bug!
        LocalDateTime dateToCheck = new LocalDateTime().plusMinutes(MINUTES_PADDING);
        if (dateToCheck.getDayOfWeek() - 1 != day)
            return false;
        return dateToCheck.toLocalTime().isAfter(getPeriod().END);
    }

    @Override
    public String toString() {
        return getDay() + ":" + getRawPeriod() + " - " + getSubject() + " in " + getRoom();
    }
}
