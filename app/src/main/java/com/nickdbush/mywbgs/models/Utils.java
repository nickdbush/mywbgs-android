package com.nickdbush.mywbgs.models;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class Utils {

    public static final String[] DAYS = {
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
    };
    public static int debugDayOffset = 0;

    // TODO: 24/09/2016 Do logic of when this school day becomes the next
    public static LocalDate getCurrentDay() {
        return LocalDate.now().plusDays(debugDayOffset);
    }

    public static LocalDate getNextSchoolDay() {
        LocalDate currentDay = LocalDate.now().plusDays(debugDayOffset);
        if (currentDay.getDayOfWeek() > 5)
            currentDay = currentDay.withDayOfWeek(1).weekOfWeekyear().addToCopy(1);
        return currentDay;
    }

    public static String getHelpfulDate(LocalDate date) {
        LocalDate today = new LocalDate();
        int difference = Days.daysBetween(today, date).getDays();
        if (difference == 0) {
            return "Today";
        } else if (difference == 1) {
            return "Tomorrow";
        } else if (difference == -1) {
            return "Yesterday";
        } else {
            return date.toString("EEEE d MMMM");
        }
    }

    public static String formatRoom(String unprocessed) {
        if (unprocessed == null || unprocessed.trim().length() == 0) return unprocessed;

        String room = unprocessed.toLowerCase().trim();
        if (room.equals("new field")) room = "nf";
        if (room.equals("sports hall")) room = "sh";
        room = room.replace("english ", "e");
        room = room.replace("biology ", "b");
        room = room.replace("chemistry ", "c");
        room = room.replace("physics ", "p");
        room = room.replace("economics ", "ec");
        room = room.replace("maths room ", "m");
        room = room.replace("technology ", "t");
        room = room.replace("art ", "a");
        room = room.replace("room ", "");
        return room.toUpperCase().trim();
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

        public static boolean isValid(int period) {
            return !(period < 0 || period >= PERIODS.length);
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
