package com.nickdbush.mywbgs.models;

import org.joda.time.LocalTime;

public class Period {

    public static final Period[] PERIODS = {
            new Period(new LocalTime(8, 55)),
            new Period(new LocalTime(9, 55)),
            new Period(new LocalTime(11, 15)),
            new Period(new LocalTime(12, 15)),
            new Period(new LocalTime(14, 15)),
            new Period(new LocalTime(15, 15))
    };

    public final LocalTime START;
    public final LocalTime END;

    public Period(LocalTime end) {
        this.START = end;
        this.END = new LocalTime(end).plusHours(1);
    }

    public Period(LocalTime start, LocalTime end) {
        this.START = start;
        this.END = end;
    }

    public boolean hasElapsed(int minutesPadding) {
        return new LocalTime().plusMinutes(minutesPadding).isAfter(END);
    }

    public String getDurationString() {
        return START.toString("HH:mm") + " - " + END.toString("HH:mm");
    }
}
