package com.examsofbharat.bramhsastra.akash.utils;

import com.examsofbharat.bramhsastra.akash.constants.AgniConstants;
import com.examsofbharat.bramhsastra.akash.constants.SystemPropertyProperties;
import com.examsofbharat.bramhsastra.prithvi.entity.SystemProperty;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class eobInitilizer {

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    Integer otpExpiryTime = 5;
    Integer otpMaxAttempts = 3;

    String otpSub;
    String siginBody;
    String signUpBody;
    private List<String> approverIdList = new ArrayList<>();

    @PostConstruct
    public void init() {
        initSystemProperties();
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


        approverIdList = Optional.ofNullable(dbMgmtFacade.getSystemProperty(SystemPropertyProperties.ADMIN_APPROVER_ID))
                .map(SystemProperty::getValue)
                .map(ids -> ids.split(AgniConstants.COMMA_DELIMETER))
                .map(Arrays::asList)
                .orElse(new ArrayList<>());

    }


    public int getOtpMaxAttempts() { return otpMaxAttempts; }
    public int getOtpExpiryTime() { return otpExpiryTime; }
    public String getOtpSub() { return otpSub; }
    public String getSiginBody() { return siginBody; }
    public String getSignUpBody() { return signUpBody; }
    public List<String> getApproverIdList() { return approverIdList; }

}