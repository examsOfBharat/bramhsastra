package com.examsofbharat.bramhsastra.prithvi.facade;

import com.examsofbharat.bramhsastra.jal.dto.ApplicationSeoDetailsDTO;
import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.examsofbharat.bramhsastra.prithvi.dao.AdmitContentManagerRepository;
import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationEligibilityRepository;
import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationNameDetailsRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.*;
import com.examsofbharat.bramhsastra.prithvi.manager.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
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
    GenericResponseV1ManagerImpl genericResponseV1Manager;

    @Autowired
    GenericContentManagerImpl genericContentManager;

    @Autowired
    AdmitContentManagerImpl admitContentManagerImp;

    @Autowired
    ResultDetailsManagerImpl resultDetailsManager;

    @Autowired
    UpcomingFormsManagerImpl upcomingFormsManager;

    @Autowired
    ResponseManagementManagerImpl responseManagementManager;

    @Autowired
    ApplicationFormManagerImpl applicationFormManager;

    @Autowired
    AdminResponseManagerImpl adminResponseManagerImpl;

    @Autowired
    ApplicationUrlManagerImpl applicationUrlManager;

    @Autowired
    ApplicationFeeDetailsManagerImpl applicationFeeDetailsManager;

    @Autowired
    ApplicationVacancyDetailsManagerImpl applicationVacancyDetailsManager;

    @Autowired
    ApplicationEligibilityManagerImpl applicationEligibilityManagerImp;

    @Autowired
    ApplicationContentManagerManagerImpl applicationContentManagerManager;

    @Autowired
    ApplicationAgeDetailsManagerImpl applicationAgeDetailsManagerImpl;

    @Autowired
    ApplicationSeoDetailsManagerImpl applicationSeoDetailsManager;

    @Autowired
    ApplicationNameDetailsManagerImpl applicationNameDetailsManagerImpl;

    @Autowired
    ResultContentManagerImpl resultContentManagerImpl;

    @Autowired
    LogoUrlManagerImpl logoUrlManager;

    @Autowired
    ApiRequestLogManagerImpl apiRequestLogManagerImpl;

    @Autowired
    private ApplicationNameDetailsRepository applicationNameDetailsRepository;
    @Autowired
    private GenericContentManagerImpl genericContentManagerImpl;

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


    public List<AdmitCard> getLatestAdmitCardList(int page, int size, String dateType){
        return admitCardManager.getLatestAdmitCards(page, size, dateType);
    }

    //generic response V1
    public List<GenericResponseV1> getLatestResponseV1List(int page, int size, String dateType, String type){
        return genericResponseV1Manager.getLatestGenericResponse(page, size, dateType,type);
    }

    public List<GenericResponseV1> getLastXDayResponseV1List(int xDays, String type){
        return genericResponseV1Manager.getXDaysResponse(xDays,type);
    }

    public GenericResponseV1 fetchResponseV1ById(String responseId){
        return genericResponseV1Manager.fetchResponseById(responseId);
    }

    public GenericResponseV1 fetchResponseV1ByAppId(String appId, String type){
        return genericResponseV1Manager.fetchResponseByAppId(appId,type);
    }

    //saving generic response v1

    public void saveGenericResponseV1(GenericResponseV1 genericResponseV1){
        genericResponseV1Manager.save(genericResponseV1);
    }

    //generic content v1

    public GenericContentManager fetchGenericContent(String contentId){
        return genericContentManagerImpl.fetchGenericContentV1(contentId);
    }

    public void saveGenericContentV1(GenericContentManager genericContentManager){
        genericContentManagerImpl.save(genericContentManager);
    }







    public List<AdmitCard> getLastXDayAdmitCardList(int xDays){
        return admitCardManager.getXDaysAdmitCards(xDays);
    }

    public List<ResultDetails> getResultDetailList(int page, int size, String dateType){
        return resultDetailsManager.getLatestResultList(page, size, dateType);
    }

    public List<UpcomingForms> fetchLatestUpcomingForm(int page, int size, String dateType){
        return upcomingFormsManager.getLatestUpcomingForm(page, size, dateType);
    }

    public void saveUpcomingForms(UpcomingForms upcomingForms){
        upcomingFormsManager.save(upcomingForms);
    }

    public List<ResultDetails> getLastXDaysResult(int xDays){
        return resultDetailsManager.getXDaysAdmitCards(xDays);
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

    public List<ApplicationForm> fetchLatestApplicationsBasedOnType(int page, int size, String subType, FormTypeEnum formTypeEnum){
        return applicationFormManager.getLatestFormBasedOnFormType(page,size,subType,formTypeEnum);
    }

    public ApplicationUrl getApplicationUrl(String appId){
        return applicationUrlManager.getApplicationUrlDetailsById(appId);
    }

    public List<ApplicationVacancyDetails> getApplicationVacancyDetails(String appId){
        return applicationVacancyDetailsManager.getAllVacancyById(appId);
    }

    public ApplicationVacancyDetails getVacancyByAppId(String appId){
        return applicationVacancyDetailsManager.getVacancyByAppId(appId);
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

    public ApplicationSeoDetails getApplicationSeoDetails(String appId){
        return applicationSeoDetailsManager.getSeoDetails(appId);
    }

    public void saveApplicationForm(ApplicationForm applicationForm){
        applicationFormManager.save(applicationForm);
    }

    public void saveApplicationUrl(ApplicationUrl applicationUrl){
        applicationUrlManager.save(applicationUrl);
    }

    public void saveApplicationFeeDetail(ApplicationFeeDatails applicationFeeDetail){
        applicationFeeDetailsManager.save(applicationFeeDetail);
    }

    public void saveApplicationVacancyDetail(List<ApplicationVacancyDetails> applicationVacancyDetail){
        applicationVacancyDetailsManager.saveAll(applicationVacancyDetail);
    }

    public void saveEligibilityDetails(List<ApplicationEligibilityDetails> eligibilityDetails){
        applicationEligibilityManagerImp.saveAll(eligibilityDetails);
    }

    public void saveApplicationAgeDetail(ApplicationAgeDetails applicationAgeDetail){
        applicationAgeDetailsManagerImpl.save(applicationAgeDetail);
    }

    public void saveApplicationSeoDetails(ApplicationSeoDetails applicationSeoDetails){
        applicationSeoDetailsManager.save(applicationSeoDetails);
    }

    public void saveApplicationContent(List<ApplicationContentManager> applicationContentManager){
        applicationContentManagerManager.saveAll(applicationContentManager);
    }

    public void saveApplicationName(ApplicationNameDetails applicationNameDetail){
        applicationNameDetailsManagerImpl.save(applicationNameDetail);
    }

    public ExamMetaData getExamMetaData(String subCat){
        return examMetaDataManagerImp.getExamMetaDataBySubCat(subCat);
    }

    public void saveExamMetaData(ExamMetaData examMetaData){
        examMetaDataManagerImp.save(examMetaData);
    }
    public void saveAdmitCard(AdmitCard admitCard){
        admitCardManager.save(admitCard);
    }

    public void saveAdmitContent(AdmitContentManager admitContentManager){
        admitContentManagerImp.save(admitContentManager);
    }

    public void saveResultDetail(ResultDetails resultDetail){
        resultDetailsManager.save(resultDetail);
    }

    public void saveResultContent(ResultContentManager resultContentManager){
        resultContentManagerImpl.save(resultContentManager);
    }

    public ResultDetails fetchResultById(String resultId){
        Optional<ResultDetails> resultDetails = resultDetailsManager.findById(resultId);
        return resultDetails.orElse(null);
    }

    public ResultContentManager fetchResultContentById(String resultId){
        return resultContentManagerImpl.fetchResultContentById(resultId);
    }

    public List<ApplicationNameDetails> fetchAllAppNames(){
        return applicationNameDetailsManagerImpl.fetchAllAppName();
    }

    public ApplicationNameDetails findNameByAppId(String appId){
        return applicationNameDetailsManagerImpl.findNameByAppId(appId);
    }

    public List<ApplicationEligibilityDetails> fetchAllEligibility(String appId){
        return applicationEligibilityManagerImp.findAllAppByAppId(appId);
    }

    public LogoUrlManager fetchLogoByName(String name){
        return logoUrlManager.findLogoByName(name);
    }

    public List<LogoUrlManager> findAllLogo(){
        List<LogoUrlManager> logpUrlList = new ArrayList<>();
        Iterable<LogoUrlManager> logoList = logoUrlManager.findAll();

        logoList.forEach(logpUrlList::add);

        return logpUrlList;
    }

    public AdmitCard fetchAdmitById(String admitId){
        return admitCardManager.fetchAdmitCardById(admitId);
    }

    public AdmitCard fetchAdmitByAppId(String appId){
        return admitCardManager.fetchAdmitByAppId(appId);
    }

    public AdmitContentManager fetchAdmitContent(String admitId){
        return admitContentManagerImp.fetchAdmitCardContent(admitId);
    }

    public List<ApplicationForm> fetchAllLatestApp(int page, int size){
        return applicationFormManager.getLatestForm(page, size);
    }

    public List<ApplicationForm> fetchAllOldestApp(int page, int size){
        return applicationFormManager.getOldestForm(page, size);
    }

    public List<ApplicationForm> getFormWithAnsKey(int page, int size, String dateType, Date startDate){
        return applicationFormManager.getFormWithAnsKey(page, size, dateType, startDate);
    }

    public List<ApplicationForm> getFormAfterDateCreated(int page, int size, String dateType, Date startDate){
        return applicationFormManager.getFormAfterDateCreated(page, size, dateType, startDate);
    }

    public AdminResponseManager findAdminResById(String resId){
        return adminResponseManagerImpl.findAdminResById(resId);
    }

    public void saveAdminResponse(AdminResponseManager adminResponseManager){
        adminResponseManagerImpl.save(adminResponseManager);
    }

    public List<AdminResponseManager> fetchAdminDataByStatus(String status){
        return adminResponseManagerImpl.fetchAdminDataByStatus(status);
    }

    public void saveRequestLog(ApiRequestLog apiRequestLog){
        apiRequestLogManagerImpl.save(apiRequestLog);
    }

}
