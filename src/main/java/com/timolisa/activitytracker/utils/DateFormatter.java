package com.timolisa.activitytracker.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static String formatDate(LocalDateTime dateTimeString) {
//        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTimeString.format(formatter);
    }
}
