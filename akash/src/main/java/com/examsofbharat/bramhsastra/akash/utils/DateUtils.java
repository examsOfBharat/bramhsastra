package com.examsofbharat.bramhsastra.akash.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class DateUtils {

    public static boolean isTimePassedMinutes(int time, Date oldDate){
        // Get the current time
        Date now = new Date();

        // Add 5 minutes to the timestamp
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        calendar.add(Calendar.MINUTE, time);
        Date fiveMinutesLater = calendar.getTime();

        // Compare the times
        return now.after(fiveMinutesLater);
    }

    public static String getFormatedDate1(Date date){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return localDate.format(formatter);
        }catch (Exception e){
            log.error("Date format exception", e);
        }
        return date.toString();
    }
}
