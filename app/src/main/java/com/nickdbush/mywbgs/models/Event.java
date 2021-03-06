package com.nickdbush.mywbgs.models;

import org.joda.time.LocalDateTime;

import java.util.Date;

import io.realm.RealmObject;

public class Event extends RealmObject {
    private String title;
    private Date start;
    private Date end;

    public Event() {

    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStart() {
        return new LocalDateTime(start);
    }

    public LocalDateTime getEnd() {
        return new LocalDateTime(end);
    }

    public String getDurationString() {
        String time;
        LocalDateTime start = getStart();
        LocalDateTime end = getEnd();

        if (start.equals(end))
            time = start.toString("HH:mm");
        else if (start.toLocalDate().equals(end.toLocalDate()))
            time = start.toString("HH:mm") + " - " + end.toString("HH:mm");
        else if (end.equals(start.plusDays(1)))
            time = "All day";
        else
            // Minus one day from end to turn exclusive to inclusive range
            time = start.toString("d MMMM") + " - " + end.minusDays(1).toString("d MMMM");

        return time;
    }

    public int getPriority() {
        LocalDateTime start = getStart();
        LocalDateTime end = getEnd();

        if (start.equals(end))
            return 0;
        else if (start.toLocalDate().equals(end.toLocalDate()))
            return 0;
        else if (end.equals(start.plusDays(1)))
            return 1;
        else
            return 2;
    }
}
