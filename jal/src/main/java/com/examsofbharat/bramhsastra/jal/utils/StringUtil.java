package com.examsofbharat.bramhsastra.jal.utils;

public class StringUtil {

    public static boolean isEmpty(String str) {
        if(str == null) return true;
        return "".equals(str.trim());
    }
}