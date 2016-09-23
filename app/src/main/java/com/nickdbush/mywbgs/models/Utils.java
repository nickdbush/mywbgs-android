package com.nickdbush.mywbgs.models;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

public class Utils {
    public static final String[] DAYS = {
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday"
    };

    public static int getClosestSchoolDay() {
        int currentDay = DateTime.now().getDayOfWeek() - 1;
        if(currentDay > 5)
            currentDay = 0;
        return currentDay;
    }

    public static class Period {
        public static final Period[] PERIODS = {
                new Period(0, new LocalTime(8, 55)),
                new Period(1, new LocalTime(9, 55)),
                new Period(2, new LocalTime(11, 15)),
                new Period(3, new LocalTime(12, 15)),
                new Period(4, new LocalTime(14, 15)),
                new Period(5, new LocalTime(15, 15))
        };

        public static boolean isValid(int period) {
            return !(period < 0 || period >= PERIODS.length);
        }

        public final LocalTime START;
        public final LocalTime END;
        public final int NUMBER;

        public Period(int number, LocalTime end) {
            this(number, end, new LocalTime(end).plusHours(1));
        }

        public Period(int number, LocalTime start, LocalTime end) {
            this.START = start;
            this.END = end;
            this.NUMBER = number;
        }

        public boolean hasElapsed(int minutesPadding) {
            return new LocalTime().plusMinutes(minutesPadding).isAfter(END);
        }

        public String getDurationString() {
            return START.toString("HH:mm") + " - " + END.toString("HH:mm");
        }

        public String getPeriodName() {
            return "P" + (NUMBER + 1);
        }
    }
}
