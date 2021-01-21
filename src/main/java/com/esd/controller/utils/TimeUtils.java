package com.esd.controller.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtils {

    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("H:m");

    public static String get24FormatHourNoSeconds(LocalTime dateTime){
        return fmt.print(dateTime);
    }



}
