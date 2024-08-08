package com.examsofbharat.bramhsastra.akash.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class DateUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

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

    public static boolean isTimePassedDays(int day, Date oldDate){
        // Get the current time
        Date now = new Date();

        // Add 5 days to the timestamp
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        calendar.add(Calendar.DATE, day);
        Date xDaysLater = calendar.getTime();

        // Compare the times
        return now.after(xDaysLater);
    }

    public static boolean isDateIsAfterXDaysNow(int day, Date date){
        // Get the current time
        Date now = new Date();

        // Add 5 days to the timestamp
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, day);
        Date xDaysLater = calendar.getTime();

        // Compare the times
        return date.after(xDaysLater);
    }

    public static String getFormatedDate1(Date date){
        try {
            if(date == null) return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return localDate.format(formatter);
        }catch (Exception e){
            log.error("Date format exception", e);
        }
        return date.toString();
    }


    public static Date removeTime(Date date) {
        try {
            if (date != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String dte =  localDate.format(formatter);
                Date date1 =  dateFormat.parse(dte);
                return date1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getNoOfDaysFromToday(Date date){
        LocalDate givenDate = convertToLocalDate(date);
        // Current date
        LocalDate today = LocalDate.now();
        // Calculate the number of days between the given date and today
        return ChronoUnit.DAYS.between(givenDate, today);
    }

    private static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static long compareDates(Date older, Date latest) {

        LocalDate date1 = convertToLocalDate(older);
        LocalDate date2 = convertToLocalDate(latest);

        // Using LocalDate's compareTo method to compare dates
        if (date1.isAfter(date2)) {
            // Calculate days between date2 and date1
            return ChronoUnit.DAYS.between(date2, date1);
        } else {
            // Return -1 (or any other indicator) if date1 is not later than date2
            return -1;
        }
    }

    public static Date addYears(Date date, int noOfYears){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(1, noOfYears);
        return calendar.getTime();
    }

    public static Date addDays(Date date, int noOfDays){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, noOfDays);
        return calendar.getTime();
    }

    public static Date addMonths(Date date, int noOfMonths){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(2, noOfMonths);
        return calendar.getTime();
    }

    //eg: fileName: form, formType: pdf then full name will be form_dd_MM_yyyy.pdf
    public static String getDateFileName(String fileName, String fileType){
        // Get today's date
        LocalDate today = LocalDate.now();

        // Format the date as yyyyMMdd
        String formattedDate = today.format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));

        // Construct the filename
        return fileName + "_" + formattedDate + "." + fileType; // Example: file_28_06_2024.txt
    }
}
