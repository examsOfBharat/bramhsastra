package com.examsofbharat.bramhsastra.akash.utils;

import java.text.MessageFormat;

public class CredUtil {

    public static String formatMailMessage(String placeHolder, String v1){
       return MessageFormat.format(placeHolder, v1);
    }

    public static String formatBodyMessage(String placeHolder, String v1, String v2, String v3, String v4){
          return MessageFormat.format(placeHolder, v1, v2, v3, v4);
    }

}
