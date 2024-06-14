package com.examsofbharat.bramhsastra.akash.utils;

import com.examsofbharat.bramhsastra.akash.constants.AkashConstants;
import com.examsofbharat.bramhsastra.akash.constants.SystemPropertyProperties;
import com.examsofbharat.bramhsastra.prithvi.entity.LogoUrlManager;
import com.examsofbharat.bramhsastra.prithvi.entity.SystemProperty;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EobInitilizer {

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    Integer otpExpiryTime = 5;
    Integer otpMaxAttempts = 3;

    private static String otpSub;
    private static String siginBody;
    private static String signUpBody;
    private static String statusMailSub;
    private static String pendingMailBody;
    private static String approvedMailBody;
    private static String rejectedMailBody;
    private List<String> approverIdList = new ArrayList<>();
    public static Map<String, String> logoMap = new HashMap<>();

    @PostConstruct
    public void init() {

        initSystemProperties();
        initLogoUrl();
    }

    private void initSystemProperties() {

        otpExpiryTime = Integer.parseInt(
                dbMgmtFacade.getSystemProperty(SystemPropertyProperties.OTP_EXPIRY_TIME_LIMIT).
                        getValue());
        otpMaxAttempts = Integer.parseInt(
                dbMgmtFacade.getSystemProperty(SystemPropertyProperties.OTP_MAX_ATTEMPTS).
                        getValue());

        otpSub = Optional.ofNullable(dbMgmtFacade.getSystemProperty(SystemPropertyProperties.OTP_MAIL_SUBJECT))
                .map(SystemProperty::getValue).orElse(SystemPropertyProperties.DEFAULT_OTP_MAIL_SUB);

        siginBody = Optional.ofNullable(dbMgmtFacade.getSystemProperty(SystemPropertyProperties.OTP_SIGN_IN_MAIL_BODY))
                .map(SystemProperty::getValue).orElse(SystemPropertyProperties.DEFAULT_SIGN_IN_BODY);

        signUpBody = Optional.ofNullable(dbMgmtFacade.getSystemProperty(SystemPropertyProperties.OTP_SIGN_UP_MAIL_BODY))
                .map(SystemProperty::getValue).orElse(SystemPropertyProperties.DEFAULT_SIGN_UP_BODY);

        statusMailSub = Optional.ofNullable(dbMgmtFacade.getSystemProperty(SystemPropertyProperties.ADMIN_STATUS_MAIL_SUBJECT))
                .map(SystemProperty::getValue).orElse(SystemPropertyProperties.DEFAULT_ADMIN_MAIL);

        pendingMailBody = Optional.ofNullable(dbMgmtFacade.getSystemProperty(SystemPropertyProperties.ADMIN_PENDING_MAIL_BODY))
                .map(SystemProperty::getValue).orElse(SystemPropertyProperties.DEFAULT_ADMIN_MAIL);

        approvedMailBody = Optional.ofNullable(dbMgmtFacade.getSystemProperty(SystemPropertyProperties.ADMIN_APPROVED_MAIL_BODY))
                .map(SystemProperty::getValue).orElse(SystemPropertyProperties.DEFAULT_ADMIN_MAIL);

        rejectedMailBody = Optional.ofNullable(dbMgmtFacade.getSystemProperty(SystemPropertyProperties.ADMIN_REJECTED_MAIL_BODY))
                .map(SystemProperty::getValue).orElse(SystemPropertyProperties.DEFAULT_ADMIN_MAIL);

        approverIdList = Optional.ofNullable(dbMgmtFacade.getSystemProperty(SystemPropertyProperties.ADMIN_APPROVER_ID))
                .map(SystemProperty::getValue)
                .map(ids -> ids.split(AkashConstants.COMMA_DELIMETER))
                .map(Arrays::asList)
                .orElse(new ArrayList<>());

    }

    public void initLogoUrl(){
        List<LogoUrlManager> logoUrlList = dbMgmtFacade.findAllLogo();
        logoMap = logoUrlList.stream()
                .collect(Collectors.toMap(logo -> logo.getName().toLowerCase(), LogoUrlManager::getLogoUrl));
    }


    public int getOtpMaxAttempts() { return otpMaxAttempts; }
    public int getOtpExpiryTime() { return otpExpiryTime; }
    public String getOtpSub() { return otpSub; }
    public String getSiginBody() { return siginBody; }
    public String getSignUpBody() { return signUpBody; }

    public String getApprovedMailBody() {return approvedMailBody;
    }
    public  String getRejectedMailBody() { return rejectedMailBody; }
    public  String getPendingMailBody() { return pendingMailBody; }
    public String getStatusMailSub(){ return statusMailSub;}

    public List<String> getApproverIdList() { return approverIdList; }

    public static String getLogoByName(String name){
        if(logoMap.containsKey(name.toLowerCase())){
            return logoMap.get(name.toLowerCase());
        }
        return null;
    }

}
