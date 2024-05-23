package com.examsofbharat.bramhsastra.akash.constants;

public class SystemPropertyProperties {

    public static final String OTP_EXPIRY_TIME_LIMIT = "otp.expiry.time.limit";
    public static final String OTP_MAX_ATTEMPTS = "otp.max.attempt.count";
    public static final String OTP_MAIL_SUBJECT = "otp.mail.subject";
    public static final String OTP_SIGN_IN_MAIL_BODY = "otp.sigin.mail.template";
    public static final String OTP_SIGN_UP_MAIL_BODY = "otp.singup.mail.template";
    public static final String ADMIN_APPROVER_ID = "admin.approver.mail.id";
    public static final String ADMIN_STATUS_MAIL_SUBJECT = "user.status.mail.subject";
    public static final String ADMIN_PENDING_MAIL_BODY = "user.pending.mail.body";
    public static final String ADMIN_REJECTED_MAIL_BODY = "user.rejected.mail.body";
    public static final String ADMIN_APPROVED_MAIL_BODY = "user.approved.mail.body";

    public static final String DEFAULT_OTP_MAIL_SUB = "Verify Your Account - OTP for {}";

    public static final String DEFAULT_SIGN_IN_BODY = "Hi {0} Thanks for singing {1}, Your OTP is {2}, it will expire in {}min";
    public static final String DEFAULT_SIGN_UP_BODY = "Hi {0} Thanks for singUp {1}, Your OTP is {2}, it will expire in {}min";
    public static final String DEFAULT_ADMIN_MAIL = "Sorry something went wrong";

}
