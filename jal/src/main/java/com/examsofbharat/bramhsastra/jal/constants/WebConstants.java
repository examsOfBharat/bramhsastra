package com.examsofbharat.bramhsastra.jal.constants;

import java.util.HashMap;
import java.util.Map;

public class WebConstants {
    public static final String ERROR = "ERROR";
    public static final String SUCCESS = "SUCCESS";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String VALID_REQUEST = "VALID_REQUEST";
    public static final String VALID = "VALID";
    public static final String INVALID = "INVALID";
    public static final String SERVER_ERROR = "SERVER_ERROR";
    public static final String INVALID_OTP = "INVALID_OTP";
    public static final String INVALID_EMAIL = "INVALID_EMAIL";
    public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String INVALID_PHONE = "INVALID_PHONE";
    public static final String INVALID_NAME = "INVALID_NAME";
    public static final String INVALID_USERNAME = "INVALID_USERNAME";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String INVALID_REQUEST = "INVALID_REQUEST";


    public static final Map<String, String> getErrorMsgMap(){
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put(INVALID_REQUEST, "Something went wrong. Please try again.");
        errorMessage.put(USER_NOT_FOUND,"User name not found. Please do signUp");
        errorMessage.put(INVALID_PHONE, "Invalid phone number");
        errorMessage.put(INVALID_EMAIL, "Invalid email");
        errorMessage.put(INVALID_PASSWORD, "Password is incorrect, Please enter correct password");
        errorMessage.put(INVALID_OTP,"OTP is incorrect, Please enter a valid OTP");

        return errorMessage;

    }

    public static final Map<String, String> getErrorTitleMap(){
        Map<String, String> errorTypes = new HashMap<>();
        errorTypes.put(SERVER_ERROR,"We're Sorry!");
        errorTypes.put(INVALID_REQUEST, "Invalid Request");
        errorTypes.put(INVALID_OTP, "Please enter a valid OTP");
        errorTypes.put(INVALID_PASSWORD, "Please enter a valid password");
        return errorTypes;

    }




}
