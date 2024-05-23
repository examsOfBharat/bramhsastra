package com.examsofbharat.bramhsastra.akash.utils;

import java.util.Date;

public class FormUtil {

    public static String getAdmitDateColor(Date date){
        boolean flag = DateUtils.isTimePassedDays(5, date);
        if(flag)
            return "#FF0000";
        return "#34EB55";
    }
}
