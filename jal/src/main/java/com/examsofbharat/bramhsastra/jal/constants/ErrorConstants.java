package com.examsofbharat.bramhsastra.jal.constants;

import java.util.HashMap;
import java.util.Map;

public class ErrorConstants {

    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String VALID_REQUEST = "VALID_REQUEST";
    public static final String VALID = "VALID";
    public static final String INVALID = "INVALID";
    public static final String SERVER_ERROR = "SERVER_ERROR";
    public static final String INVALID_OTP = "INVALID_OTP";
    public static final String EXPIRED_OTP = "EXPIRED_OTP";
    public static final String INVALID_EMAIL = "INVALID_EMAIL";
    public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String INVALID_PHONE = "INVALID_PHONE";
    public static final String INVALID_NAME = "INVALID_NAME";
    public static final String INVALID_USERNAME = "INVALID_USERNAME";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String APPROVAL_PENDING = "APPROVAL_PENDING";
    public static final String INVALID_REQUEST = "INVALID_REQUEST";
    public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    public static final String PASSWORD_MISMATCH = "PASSWORD_MISMATCH";
    public static final String USER_REJECTED = "USER_REJECTED";
    public static final String OTP_MAX_ATTEMPTS = "OTP_MAX_ATTEMPTS";
    public static final String SIGNUP_INCOMPLETE = "SIGNUP_INCOMPLETE";


    public static final Map<String, String> getErrorMsgMap(){
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put(INVALID_REQUEST, "Something went wrong. Please try again.");
        errorMessage.put(USER_NOT_FOUND,"User name not found. Please do signUp");
        errorMessage.put(INVALID_PHONE, "Invalid phone number");
        errorMessage.put(INVALID_EMAIL, "Invalid email");
        errorMessage.put(INVALID_PASSWORD, "Password is incorrect, Please enter correct password");
        errorMessage.put(INVALID_OTP,"OTP is incorrect, Please enter a valid OTP");
        errorMessage.put(USER_ALREADY_EXISTS, "User already exists, Please login");
        errorMessage.put(PASSWORD_MISMATCH, "Password is incorrect, Please try again");
        errorMessage.put(APPROVAL_PENDING, "Your approval is pending, please wait for sometime");
        errorMessage.put(EXPIRED_OTP, "Your OTP has expired, please try again");
        errorMessage.put(USER_REJECTED, "Your account has been rejected, please contact admin or concern team");
        errorMessage.put(OTP_MAX_ATTEMPTS, "OTP max attempts exceeded, please after 5 min");
        errorMessage.put(SIGNUP_INCOMPLETE, "Your Signup was incomplete, please complete your signup first");

        return errorMessage;

    }

    public static final Map<String, String> getErrorTitleMap(){
        Map<String, String> errorTypes = new HashMap<>();
        errorTypes.put(SERVER_ERROR,"We're Sorry!");
        errorTypes.put(INVALID_REQUEST, "Invalid Request");
        errorTypes.put(INVALID_OTP, "Please enter a valid OTP");
        errorTypes.put(INVALID_PASSWORD, "Please enter a valid password");
        errorTypes.put(USER_ALREADY_EXISTS, "User already exists!");
        errorTypes.put(PASSWORD_MISMATCH, "Password is incorrect");
        errorTypes.put(APPROVAL_PENDING, "Your approval is pending");
        errorTypes.put(EXPIRED_OTP, "OTP has expired");
        errorTypes.put(USER_REJECTED, "Account rejected");
        errorTypes.put(OTP_MAX_ATTEMPTS, "OTP max attempts exceeded");
        errorTypes.put(SIGNUP_INCOMPLETE, "Signup was incomplete");
        return errorTypes;

    }
}
