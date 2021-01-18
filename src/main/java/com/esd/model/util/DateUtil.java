package com.esd.model.util;

import org.joda.time.LocalTime;


public class DateUtil {

    public static LocalTime newLocalTime(int hour, int minute){
        return new LocalTime(0).withHourOfDay(hour).withMinuteOfHour(minute);
    }
}
