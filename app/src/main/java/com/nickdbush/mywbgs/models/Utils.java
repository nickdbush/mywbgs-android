package com.nickdbush.mywbgs.models;

import org.joda.time.DateTime;

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
}
