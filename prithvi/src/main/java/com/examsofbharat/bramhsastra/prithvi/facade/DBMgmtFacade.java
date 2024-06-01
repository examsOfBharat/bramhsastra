package com.examsofbharat.bramhsastra.prithvi.facade;

import com.examsofbharat.bramhsastra.prithvi.entity.*;
import com.examsofbharat.bramhsastra.prithvi.manager.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class DBMgmtFacade {

    @Autowired
    UserDetailsManagerImp userDetailsManagerImp;
    @Autowired
    private SystemRepositoryManagerImp systemPropertyManager;

    @Autowired
    ExamMetaDataManagerImp examMetaDataManagerImp;

    @Autowired
    AdmitCardManagerImpl admitCardManager;

    @Autowired
    ResultDetailsManagerImpl resultDetailsManager;

    @Autowired
    ResponseManagementManagerImpl responseManagementManager;

    @Autowired
    ApplicationFormManagerImpl applicationFormManager;

    @Autowired
    ApplicationUrlManagerImpl applicationUrlManager;

    @Autowired
    ApplicationFeeDetailsManagerImpl applicationFeeDetailsManager;

    @Autowired
    ApplicationVacancyDetailsManagerImpl applicationVacancyDetailsManager;

    @Autowired
    ApplicationContentManagerManagerImpl applicationContentManagerManager;
    @Autowired
    private ApplicationAgeDetailsManagerImpl applicationAgeDetailsManagerImpl;

    public UserDetails getUserDetails(String userId){
        Optional<UserDetails> userDetails = userDetailsManagerImp.findUserByUserId(userId);
        return userDetails.orElse(null);
    }

    public UserDetails getUserById(String userId){
        return userDetailsManagerImp.findUserById(userId);
    }

    public List<UserDetails> getUserDetailsByUserStatus(UserDetails.UserStatus userStatus){
        return userDetailsManagerImp.findUserByUserStatus(userStatus);
    }

    public UserDetails findUserByUserId(String userId){
        return userDetailsManagerImp.findUserById(userId);
    }

    public UserDetails saveUserDetails(UserDetails userDetails){
        return userDetailsManagerImp.save(userDetails);
    }

    public SystemProperty getSystemProperty(String propertyName){
        return systemPropertyManager.getSystemProperty(propertyName);
    }

    public void updateUserDetails(UserDetails userDetails){
        userDetailsManagerImp.save(userDetails);
    }

    public void deleteUserDetails(UserDetails userDetails){
        userDetailsManagerImp.delete(userDetails);
    }

    public List<ExamMetaData> getExamMetaData(){
        return examMetaDataManagerImp.getExamMetaData();
    }

    public List<AdmitCard> getAdmitCardList(int count, String dateType){
        return admitCardManager.getTopXAdmitCard(count, dateType).get();
    }

    public List<ResultDetails> getResultDetailList(int count, String dateType){
        return resultDetailsManager.getTopXResult(count, dateType).get();
    }

    public void saveResponseData(ResponseManagement responseManagement){
        responseManagementManager.save(responseManagement);
    }

    public ResponseManagement getResponseData(String responseType){
        return responseManagementManager.fetchResponseByType(responseType);
    }


    public ApplicationForm getApplicationForm(String appId){
        return applicationFormManager.getApplicationFormById(appId);
    }

    public ApplicationUrl getApplicationUrl(String appId){
        return applicationUrlManager.getApplicationUrlDetailsById(appId);
    }

    public List<ApplicationVacancyDetails> getApplicationVacancyDetails(String appId){
        return applicationVacancyDetailsManager.getAllVacancyById(appId);
    }

    public List<ApplicationContentManager> getApplicationContentDetails(String appId){
        return applicationContentManagerManager.getAllContentById(appId);
    }

    public ApplicationFeeDatails getApplicationFeeDetails(String appId){
        return applicationFeeDetailsManager.getApplicationAgeDetailsById(appId);
    }

    public ApplicationAgeDetails getApplicationAgeDetails(String appId){
        return applicationAgeDetailsManagerImpl.getApplicationAgeDetailsById(appId);
    }

}
