package com.examsofbharat.bramhsastra.prithvi.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date addDays(Date date, int noOfDays){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, noOfDays);
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date day){ return getStartOfDay(day, Calendar.getInstance());}

    public static Date getStartOfDay(Date day, Calendar calendar){
        if(day == null){
            day = new Date();
        }

        calendar.setTime(day);
        calendar.set(11, calendar.getMinimum(11));
        calendar.set(12, calendar.getMinimum(12));
        calendar.set(13, calendar.getMinimum(13));
        calendar.set(14, calendar.getMinimum(14));
        return calendar.getTime();
    }

    public static Date getEndOfDay(Date day){ return getEndOfDay(day, Calendar.getInstance());}

    public static Date getEndOfDay(Date day, Calendar calendar){
        if(day == null){
            day = new Date();
        }

        calendar.setTime(day);
        calendar.set(11, calendar.getMaximum(11));
        calendar.set(12, calendar.getMaximum(12));
        calendar.set(13, calendar.getMaximum(13));
        calendar.set(14, calendar.getMaximum(14));
        return calendar.getTime();
    }
}
