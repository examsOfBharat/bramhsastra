package com.examsofbharat.bramhsastra.akash.service.adminService;

import com.examsofbharat.bramhsastra.akash.constants.AkashConstants;
import com.examsofbharat.bramhsastra.akash.executor.FormExecutorService;
import com.examsofbharat.bramhsastra.akash.service.mailService.EmailService;
import com.examsofbharat.bramhsastra.akash.utils.*;
import com.examsofbharat.bramhsastra.jal.constants.ErrorConstants;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
import com.examsofbharat.bramhsastra.jal.dto.request.OwnerLandingRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.LogInDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.RegisterDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.OwnerLandingResponseDTO;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.entity.UserDetails;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CredService {

    @Autowired
    WebUtils webUtils;

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    @Autowired
    EmailService emailService;

    @Autowired
    OtpService otpService;

    @Autowired
    EmailUtil emailUtil;

    @Autowired
    private EobInitilizer eobInitilizer;

    //Check if user already exist or not
    public Response doUserSignUp(RegisterDTO registerDTO) {
        if (Objects.isNull(registerDTO)) {
            return webUtils.invalidRequest();
        }

        UserDetails userDetails = dbMgmtFacade.getUserDetails(registerDTO.getUserName());
        if (Objects.isNull(userDetails) || userDetails.getStatus().equals(UserDetails.Status.CREATED)) {
            //TODO if userDetails in present then either update or delete the old entries
            if(Objects.nonNull(userDetails)){
                dbMgmtFacade.deleteUserDetails(userDetails);
            }
            return proceedSignUp(registerDTO);
        }
        return webUtils.userAlreadyExistsError();
    }

    //This method is for login,
    //Handle both otp and password login system
    public Response doUserLogIn(LogInDTO logInDTO) {
        if (Objects.isNull(logInDTO)) {
            return webUtils.invalidRequest();
        }

        UserDetails userDetails = dbMgmtFacade.getUserDetails(logInDTO.getUserName());
        if (Objects.isNull(userDetails)) {
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.USER_NOT_FOUND);
        }

        if (userDetails.getStatus().equals(UserDetails.Status.CREATED)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SIGNUP_INCOMPLETE);
        }

        if (userDetails.getUserStatus().equals(UserDetails.UserStatus.REJECTED)) {
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.USER_REJECTED);
        }

        if (userDetails.getUserStatus().equals(UserDetails.UserStatus.PENDING)) {
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.APPROVAL_PENDING);
        }

        //If request contains otpFlag true, then we skip password flow
        if (logInDTO.isOtpFlag()) {
            //generate otp, send email and save to table
            return createAndSendOtp(userDetails, AkashConstants.SIGN_IN);
        }

        if (isValidPassword(logInDTO, userDetails)) {
            return webUtils.buildSuccessResponseWithData(WebConstants.SUCCESS, userDetails.getId(), userDetails.getUserRole().toString());
        }

        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.PASSWORD_MISMATCH);
    }


    public Response verifyOtp(LogInDTO logInDTO) {
        if (Objects.isNull(logInDTO)) {
            return webUtils.invalidRequest();
        }

        UserDetails userDetails = dbMgmtFacade.getUserDetails(logInDTO.getUserName());

        if (!DateUtils.isTimePassedMinutes(eobInitilizer.getOtpExpiryTime(), userDetails.getDateModified())) {
            if (userDetails.getOtp().equalsIgnoreCase(logInDTO.getOtp())) {
                if (userDetails.getStatus().equals(UserDetails.Status.CREATED)) {
                    userDetails.setStatus(UserDetails.Status.VERIFIED);
                    dbMgmtFacade.updateUserDetails(userDetails);
                }
                return webUtils.buildSuccessResponseWithData(
                        WebConstants.SUCCESS, userDetails.getId(), userDetails.getUserRole().toString());
            }
        } else {
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.EXPIRED_OTP);
        }

        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.INVALID_OTP);
    }

    public Response getUserDetails(OwnerLandingRequestDTO ownerLandingRequestDTO) {
        if (Objects.isNull(ownerLandingRequestDTO)) {
            return webUtils.invalidRequest();
        }
        UserDetails userDetails = dbMgmtFacade.findUserByUserId(ownerLandingRequestDTO.getUserId());
        if (Objects.isNull(userDetails)) {
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.USER_NOT_FOUND);
        }

        List<OwnerLandingResponseDTO> ownerResList =  new ArrayList<>();
        if(userDetails.getUserRole().equals(UserDetails.UserRole.OWNER)){
            List<UserDetails> userList = dbMgmtFacade.getUserDetailsByUserStatus(
                    UserDetails.UserStatus.valueOf(ownerLandingRequestDTO.getUserStatus()));

            if(CollectionUtils.isEmpty(userList)){
                return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.USER_NOT_FOUND);
            }
            //build owner response
            ownerResList = buildOwnerResponse(userList);

            if(CollectionUtils.isEmpty(ownerResList)){
                return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.USER_NOT_FOUND);
            }

        }

        return Response.ok(ownerResList).build();
    }

    public Response updateUserStatus(OwnerLandingRequestDTO ownerLandingRequestDTO) {
        if(Objects.isNull(ownerLandingRequestDTO)) {
            return webUtils.invalidRequest();
        }
        log.info("Updating user status userId ::{}", ownerLandingRequestDTO.getUserId());
        UserDetails ownerUserDetail = dbMgmtFacade.getUserById(ownerLandingRequestDTO.getOwnerUserId());


        UserDetails userDetails = dbMgmtFacade.getUserById(ownerLandingRequestDTO.getUserId());
        if (Objects.isNull(userDetails)) {
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.USER_NOT_FOUND);
        }

        userDetails.setUserStatus(
                UserDetails.UserStatus.valueOf(ownerLandingRequestDTO.getUserStatus())
        );
        userDetails.setApprover(ownerUserDetail.getFirstName());
        dbMgmtFacade.updateUserDetails(userDetails);

        FormExecutorService.mailExecutorService.submit(()->
                sendUserStatusMail(userDetails));

        return webUtils.buildSuccessResponse("SUCCESS");
    }


    public boolean isValidPassword(LogInDTO logInDTO, UserDetails userDetails) {
        if (StringUtil.isEmpty(logInDTO.getPassWord())) return false;

        String hashPassword = BCrypt.hashpw(logInDTO.getPassWord(), userDetails.getPassWord());

        if (hashPassword.equals(userDetails.getPassWord())) {
            return true;
        }
        return false;
    }

    //Build user response and save to db
    private Response proceedSignUp(RegisterDTO registerDTO) {
        //Build userDetails object with flag false (not valid)
        UserDetails userDetails = buildUserDetails(registerDTO);
        return createAndSendOtp(userDetails, AkashConstants.SIGN_UP);
    }

    public Response createAndSendOtp(UserDetails userDetails, String service) {

        if (isOtpMaxAttemptsExceeded(userDetails)) {
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.OTP_MAX_ATTEMPTS);
        }

        String otp = otpService.generateOTP();
        userDetails = updateUserDetailsWithOtp(userDetails, otp);

        log.info("sending otp email userId ::{} " ,userDetails.getId());

        UserDetails finalUserDetails = userDetails;
        FormExecutorService.mailExecutorService.submit(()->{
            sendOtpMail(finalUserDetails, service);
        });

        return webUtils.buildSuccessResponseWithData(WebConstants.SUCCESS, userDetails.getId(), userDetails.getUserRole().toString());
    }

    private UserDetails buildUserDetails(RegisterDTO registerDTO){
        UserDetails userDetails = new UserDetails();

        userDetails.setFirstName(registerDTO.getFirstName());
        userDetails.setLastName(registerDTO.getLastName());
        userDetails.setAttempts(-1);
        userDetails.setUserRole(UserDetails.UserRole.valueOf(registerDTO.getUserRole()));
        userDetails.setUserStatus(UserDetails.UserStatus.PENDING);
        userDetails.setStatus(UserDetails.Status.CREATED);
        userDetails.setEmailId(registerDTO.getUserName());
        userDetails.setPassWord(BCrypt.hashpw(registerDTO.getPassWord(), BCrypt.gensalt()));
        userDetails.setPhoneNumber(registerDTO.getPhoneNumber());
        userDetails.setDateCreated(new Date());
        userDetails.setDateModified(new Date());

        return userDetails;
    }

    private UserDetails updateUserDetailsWithOtp(UserDetails userDetails, String otp) {
        //if user requesting for otp within x min, keep increasing the attempts
        if (!DateUtils.isTimePassedMinutes(eobInitilizer.getOtpExpiryTime(), userDetails.getDateModified())) {
            userDetails.setAttempts(userDetails.getAttempts() + 1);
        }
        userDetails.setOtp(otp);
        userDetails.setDateModified(new Date());
        return dbMgmtFacade.saveUserDetails(userDetails);
    }

    private String getOtpMailSub(){
        String sub = eobInitilizer.getOtpSub();
        return CredUtil.formatMailMessage(sub, AkashConstants.EXAMS_OF_BHARAT);
    }

    private void sendOtpMail(UserDetails userDetails, String service) {
        String getOtpSub = getOtpMailSub();
        String mailBody = getOtpMailBody(service, userDetails);
        emailService.sendEmail(userDetails.getEmailId(), getOtpSub, mailBody);
    }

    private String getOtpMailBody(String service, UserDetails userDetails) {
        String bodyTemplate;
        if (service.equalsIgnoreCase(AkashConstants.SIGN_IN)) {
            bodyTemplate = eobInitilizer.getSiginBody();
        } else {
            bodyTemplate = eobInitilizer.getSignUpBody();
        }

        return CredUtil.formatBodyMessage(bodyTemplate,
                userDetails.getFirstName(),
                AkashConstants.EXAMS_OF_BHARAT,
                userDetails.getOtp(),
                String.valueOf(eobInitilizer.getOtpExpiryTime()));

    }

    private boolean isOtpMaxAttemptsExceeded(UserDetails userDetails) {
        if (DateUtils.isTimePassedMinutes(eobInitilizer.getOtpExpiryTime(), userDetails.getDateModified())) {
            userDetails.setAttempts(1);
            return false;
        }
        int maxAttempts = eobInitilizer.getOtpMaxAttempts();
        return userDetails.getAttempts() >= maxAttempts;
    }

    private List<OwnerLandingResponseDTO> buildOwnerResponse(List<UserDetails> userDetails) {
        List<OwnerLandingResponseDTO> ownerResponseList = new ArrayList<>();
        for (UserDetails userDetail : userDetails) {
            if(userDetail.getUserRole().equals(UserDetails.UserRole.OWNER)){
                continue;
            }
            OwnerLandingResponseDTO ownerRes= new OwnerLandingResponseDTO();
            ownerRes.setFirstName(userDetail.getFirstName());
            ownerRes.setLastName(userDetail.getLastName());
            ownerRes.setEmail(userDetail.getEmailId());
            ownerRes.setPhone(userDetail.getPhoneNumber());
            ownerRes.setUserId(userDetail.getId());
            ownerRes.setStatus(userDetail.getUserStatus().name());
            ownerRes.setOwnerRole(userDetail.getUserRole().name());
            ownerRes.setRegisteredTime(DateUtils.getFormatedDate1(userDetail.getDateModified()));

            ownerResponseList.add(ownerRes);
        }

        return ownerResponseList;
    }

    public void sendUserStatusMail(UserDetails userDetails) {
        String mailSub = eobInitilizer.getStatusMailSub();
        String mailBody = CredUtil.formatMailMessage(emailUtil.getMailBodyByStatus(userDetails.getUserStatus().name()),
                userDetails.getFirstName());

        emailService.sendEmail(userDetails.getEmailId(), mailSub, mailBody);
    }
}
