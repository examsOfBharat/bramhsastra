package com.examsofbharat.bramhsastra.akash.service.adminService;

import com.examsofbharat.bramhsastra.akash.service.mailService.EmailService;
import com.examsofbharat.bramhsastra.akash.service.clientService.ResponseManagementService;
import com.examsofbharat.bramhsastra.akash.service.mailService.MailUtils;
import com.examsofbharat.bramhsastra.akash.utils.*;
import com.examsofbharat.bramhsastra.akash.validator.FormValidator;
import com.examsofbharat.bramhsastra.jal.constants.ErrorConstants;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
import com.examsofbharat.bramhsastra.jal.dto.*;
import com.examsofbharat.bramhsastra.jal.dto.request.*;
import com.examsofbharat.bramhsastra.jal.dto.request.admin.AdminGenericResponseV1;
import com.examsofbharat.bramhsastra.jal.dto.request.admin.WrapperGenericAdminResponseV1DTO;
import com.examsofbharat.bramhsastra.jal.dto.response.AdminResponseDataDTO;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.StatusEnum;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.entity.*;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import com.examsofbharat.bramhsastra.prithvi.manager.ApplicationNameDetailsManagerImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.List;

@Service
@Slf4j
public class FormAdminService {

    @Autowired
    WebUtils webUtils;

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    @Autowired
    ResponseManagementService responseManagementService;;

    @Autowired
    ApplicationNameDetailsManagerImpl applicationNameDetailsManager;

    @Autowired
    EmailService emailService;

    @Autowired
    MailUtils mailUtils;

    ObjectMapper mapper = new ObjectMapper();


    /**
     * Once admin submit form, put it in temporary table
     * send mail for review along with pdf page
     * @param enrichedFormDetailsDTO @mandatory
     */
    public Response processAndSaveAdminFormResponse(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        if(Objects.isNull(enrichedFormDetailsDTO) ||
                Objects.isNull(enrichedFormDetailsDTO.getAdminUserDetailsDTO())){
            return webUtils.invalidRequest();
        }
        try {
            AdminUserDetailsDTO adminUserDetailsDTO = enrichedFormDetailsDTO.getAdminUserDetailsDTO();

            UserDetails userDetails = dbMgmtFacade.findUserByUserId(adminUserDetailsDTO.getUserId());
            String response = new Gson().toJson(enrichedFormDetailsDTO);
            AdminResponseManager adminResponseManager = buildAdminResponse(response, userDetails, "form");

            dbMgmtFacade.saveAdminResponse(adminResponseManager);
            mailUtils.buildFormPdfAndSendMail(enrichedFormDetailsDTO, userDetails);

            return webUtils.buildSuccessResponse(WebConstants.SUCCESS);
        }catch (Exception e){
            log.info("Exception occurred while submitting form",e);
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }


    /**
     * Once we receive ADMIT/RESULT/ANS KEY, put it in temporary table
     * send mail for review along with pdf page
     * @param wrapperGenericAdminResponseV1DTO @mandatory
     */
    public Response processAndSaveGenericResV1(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO){
        if(Objects.isNull(wrapperGenericAdminResponseV1DTO) ||
                Objects.isNull(wrapperGenericAdminResponseV1DTO.getAdminUserDetailsDTO()) ||
                Objects.isNull(wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1())){

            log.info("Invalid save admit card request");
            return webUtils.invalidRequest();
        }

        try{
            String response = new Gson().toJson(wrapperGenericAdminResponseV1DTO);

            UserDetails userDetails = dbMgmtFacade.findUserByUserId(wrapperGenericAdminResponseV1DTO.
                    getAdminUserDetailsDTO().getUserId());

            AdminResponseManager adminResponseManager = buildAdminResponse(response, userDetails,
                    wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1().getPageType());

            dbMgmtFacade.saveAdminResponse(adminResponseManager);

//            buildResultPdfAndSendMail(wrapperAnsKeyAdminResDTO.getAdminUserDetailsDTO(), userDetails);
            return Response.ok().build();
        }catch (Exception e){
            log.info("Exception occurred while submitting form",e);
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }

    /**
     * Once we receive ADMIT/RESULT/ANS KEY, put it in temporary table
     * send mail for review along with pdf page
     * @param wrapperGenericAdminResponseV1DTO @mandatory
     */
    public Response processAndSaveUpcomingForm(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO){
        if(Objects.isNull(wrapperGenericAdminResponseV1DTO) ||
                Objects.isNull(wrapperGenericAdminResponseV1DTO.getAdminUserDetailsDTO()) ||
                Objects.isNull(wrapperGenericAdminResponseV1DTO.getUpcomingFormsAdminResDTO())){

            log.info("Invalid save upcoming request");
            return webUtils.invalidRequest();
        }

        try{
            String response = new Gson().toJson(wrapperGenericAdminResponseV1DTO);

            UserDetails userDetails = dbMgmtFacade.findUserByUserId(wrapperGenericAdminResponseV1DTO.
                    getAdminUserDetailsDTO().getUserId());

            AdminResponseManager adminResponseManager = buildAdminResponse(response, userDetails,
                    wrapperGenericAdminResponseV1DTO.getUpcomingFormsAdminResDTO().getType());

            dbMgmtFacade.saveAdminResponse(adminResponseManager);

//            buildResultPdfAndSendMail(wrapperAnsKeyAdminResDTO.getAdminUserDetailsDTO(), userDetails);
            return Response.ok().build();
        }catch (Exception e){
            log.info("Exception occurred while submitting form",e);
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }


    /**
     * Once reviewer will approve the admin submitted form.
     * form data will be inserted in respective tables
     * After this user will be able to see the form
     * @param enrichedFormDetailsDTO @mandatory
     */
    public Response saveFormToDb(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        if(!FormValidator.isValidFormRequest(enrichedFormDetailsDTO)){
            return webUtils.invalidRequest();
        }

        final String examId = UUIDUtil.generateUUID();
        log.info("Saving form with id {}", examId);

        final Date dateCreated = new Date();

        ApplicationFormDTO applicationFormDTO = enrichedFormDetailsDTO.getApplicationFormDTO();
        applicationFormDTO.setId(examId);
        applicationFormDTO.setDateCreated(dateCreated);
        if(applicationFormDTO.getProvince().equalsIgnoreCase(FormSubTypeEnum.CENTRAL.name())){
            applicationFormDTO.setState(FormSubTypeEnum.CENTRAL.name());
        }
        applicationFormDTO.setDateModified(dateCreated);
        dbMgmtFacade.saveApplicationForm(mapper.convertValue(applicationFormDTO, ApplicationForm.class));


        ApplicationAgeDetailsDTO applicationAgeDetailsDTO = enrichedFormDetailsDTO.getApplicationAgeDetailsDTO();
        applicationAgeDetailsDTO.setId(UUIDUtil.generateUUID());
        applicationAgeDetailsDTO.setAppIdRef(examId);
        applicationAgeDetailsDTO.setDateCreated(dateCreated);
        applicationAgeDetailsDTO.setDateModified(dateCreated);
        dbMgmtFacade.saveApplicationAgeDetail(mapper.convertValue(applicationAgeDetailsDTO, ApplicationAgeDetails.class));

        if(Objects.nonNull(enrichedFormDetailsDTO.getApplicationSeoDetailsDTO())) {
            saveSeoDetails(enrichedFormDetailsDTO.getApplicationSeoDetailsDTO(), examId);
        }

        ApplicationFeeDTO applicationFeeDTO = enrichedFormDetailsDTO.getApplicationFeeDTO();
        applicationFeeDTO.setId(UUIDUtil.generateUUID());
        applicationFeeDTO.setAppIdRef(examId);
        applicationFeeDTO.setDateCreated(dateCreated);
        applicationFeeDTO.setDateModified(dateCreated);
        applicationFeeDTO.setLastPaymentDate(applicationFeeDTO.getLastPaymentDate());
        dbMgmtFacade.saveApplicationFeeDetail(mapper.convertValue(applicationFeeDTO, ApplicationFeeDatails.class));

        ApplicationUrlsDTO applicationUrlsDTO = enrichedFormDetailsDTO.getApplicationUrlsDTO();
        applicationUrlsDTO.setId(UUIDUtil.generateUUID());
        applicationUrlsDTO.setAppIdRef(examId);
        applicationUrlsDTO.setDateCreated(dateCreated);
        applicationUrlsDTO.setDateModified(dateCreated);

        ApplicationUrl applicationUrl = mapper.convertValue(applicationUrlsDTO, ApplicationUrl.class);
        applicationUrl.setOthers(applicationUrlsDTO.getExtra());
        dbMgmtFacade.saveApplicationUrl(applicationUrl);


        List<ApplicationContentManagerDTO> applicationContentManagerDTOList = enrichedFormDetailsDTO.getApplicationContentManagerDTO();
        List<ApplicationContentManager> formContentList = new ArrayList<>();

        if(applicationContentManagerDTOList != null && !applicationContentManagerDTOList.isEmpty()){
            applicationContentManagerDTOList.forEach(formContentManagerDTO -> {
                formContentManagerDTO.setId(UUIDUtil.generateUUID());
                formContentManagerDTO.setAppIdRef(examId);
                formContentManagerDTO.setDateCreated(dateCreated);
                formContentManagerDTO.setDateModified(dateCreated);

                formContentList.add(mapper.convertValue(formContentManagerDTO, ApplicationContentManager.class));
            });
            dbMgmtFacade.saveApplicationContent(formContentList);
        }

        List<ApplicationVacancyDTO> applicationVacancyDTOList = enrichedFormDetailsDTO.getApplicationVacancyDTOS();
        List<ApplicationVacancyDetails> vacancyDetailList = new ArrayList<>();
        List<ApplicationEligibilityDetails> eligibilityDetailsList = new ArrayList<>();

        if(applicationVacancyDTOList != null && !applicationVacancyDTOList.isEmpty()){
            final int[] i = {1};
            applicationVacancyDTOList.forEach(formVacancyDTO -> {
                buildAndSaveVacancy(formVacancyDTO, examId, vacancyDetailList, i[0]);
                buildAndSaveEligibility(formVacancyDTO, examId,eligibilityDetailsList, i[0]);
                i[0]++;
            });
            dbMgmtFacade.saveApplicationVacancyDetail(vacancyDetailList);
            dbMgmtFacade.saveEligibilityDetails(eligibilityDetailsList);
        }

        //save application name and type
        saveAppNameDetails(examId, applicationFormDTO.getExamName(),dateCreated, "form");

        //save and update application meta-data
        saveOrUpdateApplicationMetaData(applicationFormDTO, applicationVacancyDTOList);

        return Response.ok().build();
    }

    //build vacancy details
    public void buildAndSaveVacancy(ApplicationVacancyDTO applicationVacancyDTO, String examId,
                                    List<ApplicationVacancyDetails> vacancyDetailList , int i) {

        applicationVacancyDTO.setId(UUIDUtil.generateUUID());
        applicationVacancyDTO.setAppIdRef(examId);
        applicationVacancyDTO.setSequence(i);
        applicationVacancyDTO.setDateCreated(new Date());
        applicationVacancyDTO.setDateModified(new Date());

        vacancyDetailList.add(mapper.convertValue(applicationVacancyDTO, ApplicationVacancyDetails.class));
    }


    //build eligibility details
    public void buildAndSaveEligibility(ApplicationVacancyDTO applicationVacancyDTO, String examId,
                                        List<ApplicationEligibilityDetails> eligibilityDetailsList , int i){
        ApplicationEligibilityDTO eligibilityDTO = new ApplicationEligibilityDTO();

        eligibilityDTO.setId(UUIDUtil.generateUUID());
        eligibilityDTO.setAppIdRef(examId);
        eligibilityDTO.setSequence(i);
        eligibilityDTO.setAgeRange(applicationVacancyDTO.getAgeRange());
        eligibilityDTO.setExperience(applicationVacancyDTO.getExperience());
        eligibilityDTO.setQualification(applicationVacancyDTO.getQualification());
        eligibilityDTO.setDateCreated(new Date());
        eligibilityDTO.setDateModified(new Date());

        eligibilityDetailsList.add(mapper.convertValue(eligibilityDTO, ApplicationEligibilityDetails.class));
    }


    private void saveSeoDetails(ApplicationSeoDetailsDTO applicationSeoDetailsDTO, String formId){

        applicationSeoDetailsDTO.setId(UUIDUtil.generateUUID());
        applicationSeoDetailsDTO.setAppIdRef(formId);
        applicationSeoDetailsDTO.setDateCreated(new Date());
        applicationSeoDetailsDTO.setDateModified(new Date());
        dbMgmtFacade.saveApplicationSeoDetails(mapper.convertValue(applicationSeoDetailsDTO, ApplicationSeoDetails.class));
    }

    /**
     * Meta-data table contains collective information of each form type
     * eg: for each new form entry we categorise them in respective section.
     * like 1 form can belong to particular state, particular sector and particular qualification
     * update all of them with required data to prove landing page data.
     * @param applicationFormDTO @mandatory
     * @param applicationVacancyDTOList @mandatory
     */
    private void saveOrUpdateApplicationMetaData(ApplicationFormDTO applicationFormDTO,
                                                 List<ApplicationVacancyDTO> applicationVacancyDTOList){

        //if vacancy itself is null, then nothing to update in meta-data table
        if(CollectionUtils.isEmpty(applicationVacancyDTOList)){
            return;
        }

        List<String> categoryList = new ArrayList<>();
        categoryList.add(applicationFormDTO.getProvince());
        categoryList.add(applicationFormDTO.getGrade());
        categoryList.add(applicationFormDTO.getMinQualification());
        categoryList.add(applicationFormDTO.getSectors());


        for(String subCategory : categoryList){
            ExamMetaData examMetaData = dbMgmtFacade.getExamMetaData(subCategory);
            if(Objects.nonNull(examMetaData)){
            examMetaData.setTotalVacancy(examMetaData.getTotalVacancy() + applicationFormDTO.getTotalVacancy());
            examMetaData.setTotalForm(examMetaData.getTotalForm()+1);
            examMetaData.setDateModified(new Date());
            }else {
                examMetaData = new ExamMetaData();
                examMetaData.setTotalVacancy(applicationFormDTO.getTotalVacancy());
                examMetaData.setTotalForm(1);
                examMetaData.setDateModified(new Date());
                examMetaData.setDateCreated(new Date());
            }
            dbMgmtFacade.saveExamMetaData(examMetaData);
        }

        //Update Home page data
        updateHomeCount(applicationFormDTO.getTotalVacancy());

        //once we get new data, will update landing page response to reflect updated data
        responseManagementService.buildAndUpdateClientHomePage();
    }

    public void updateMetaData(String subCategory){
        ExamMetaData examMetaData = dbMgmtFacade.getExamMetaData(subCategory);
        if(Objects.nonNull(examMetaData)){
            examMetaData.setTotalForm(examMetaData.getTotalForm()+1);
            examMetaData.setDateModified(new Date());
        }else {
            examMetaData = new ExamMetaData();
            examMetaData.setTotalForm(1);
            examMetaData.setDateModified(new Date());
            examMetaData.setDateCreated(new Date());
        }
        dbMgmtFacade.saveExamMetaData(examMetaData);
    }

    //User for home page heading counting value
    public void updateHomeCount(int totalVacancy){
        ResponseManagement responseManagement;
        responseManagement = dbMgmtFacade.getResponseData("COUNT_UPDATES");
        HomePageCountDataDTO homePageCountDataDTO;
        if(Objects.nonNull(responseManagement)){
            homePageCountDataDTO = new Gson().fromJson(responseManagement.getResponse(), HomePageCountDataDTO.class);
            homePageCountDataDTO.setTotalForms(homePageCountDataDTO.getTotalForms() + 1);
            homePageCountDataDTO.setTotalVacancy(
                    String.valueOf(
                            Integer.parseInt(homePageCountDataDTO.getTotalVacancy()
                            ) + totalVacancy));

            String response = new Gson().toJson(homePageCountDataDTO);
            responseManagement.setResponse(response);
            responseManagement.setDateModified(new Date());
            dbMgmtFacade.saveResponseData(responseManagement);
        }
    }

    //update admit card id in application form
    private void updateAdmitIdInApplication(String appId, String admitId){
        ApplicationForm applicationForm = dbMgmtFacade.getApplicationForm(appId);
        if(Objects.nonNull(applicationForm)){
            applicationForm.setAdmitId(admitId);
            dbMgmtFacade.saveApplicationForm(applicationForm);
        }
    }

    /**
     * Once owner/admin approve the forms
     * ADMIT/RESULT/ANS KEY will be saved to generic table.
     */
    public Response buildAndSaveGenericDataV1(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO,
                                              String pageType) {

        AdminGenericResponseV1 adminGenericResponseV1 = wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1();

        if (Objects.isNull(adminGenericResponseV1)) {
            webUtils.invalidRequest();
        }

        String formId = UUIDUtil.generateUUID();

        buildAndSaveAdminGenResV1(adminGenericResponseV1, formId, pageType);

        buildAndSaveAdminGenContent(adminGenericResponseV1, formId);

        //update application form for admit card and result
        if(StringUtil.notEmpty(adminGenericResponseV1.getAppIdRef())){
            ApplicationForm applicationForm = dbMgmtFacade.getApplicationForm(adminGenericResponseV1.getAppIdRef());
            if(Objects.nonNull(applicationForm)){
                if(pageType.equalsIgnoreCase("admit"))
                    applicationForm.setAdmitId(formId);
                else if(pageType.equalsIgnoreCase("result"))
                    applicationForm.setResultId(formId);
            }
            dbMgmtFacade.saveApplicationForm(applicationForm);
        }

        if(Objects.nonNull(wrapperGenericAdminResponseV1DTO.getApplicationSeoDetailsDTO())){
            saveSeoDetails(wrapperGenericAdminResponseV1DTO.getApplicationSeoDetailsDTO(), formId);
        }

        saveAppNameDetails(formId, adminGenericResponseV1.getTitle(), new Date(), pageType);

        //update meta-data table
        //TODO need to make unique key through out the project
        //one place admit, other place enum ADMIT so we need to make it single as ADMIT
        String pageSubType = null;
        if(pageType.equalsIgnoreCase("admit"))
            pageSubType = FormSubTypeEnum.ADMIT.name();
        else if(pageType.equalsIgnoreCase("result"))
            pageSubType = FormSubTypeEnum.RESULT.name();
        else if(pageType.equalsIgnoreCase("anskey"))
            pageSubType = FormSubTypeEnum.ANS_KEY.name();

        if(StringUtil.notEmpty(pageSubType)){
            log.info("Updating meta-data for ::{}", pageSubType);
            updateMetaData(pageSubType);
        }else{
            log.info("Not able to Update meta-data for ::{}", pageSubType);
        }
        return webUtils.buildSuccessResponse("SUCCESS");
    }

    public void buildAndSaveAdminGenResV1(AdminGenericResponseV1 adminGenericResponseV1,
                                          String formId, String pageSubType){


        GenericResponseV1 genericResponseV1 = new GenericResponseV1();

        genericResponseV1.setTitle(adminGenericResponseV1.getTitle());
        genericResponseV1.setId(formId);
        genericResponseV1.setAppIdRef(adminGenericResponseV1.getAppIdRef());
        genericResponseV1.setType(pageSubType);
        genericResponseV1.setDownloadUrl(adminGenericResponseV1.getDownloadUrl());
        genericResponseV1.setShowDate(adminGenericResponseV1.getShowDate());
        genericResponseV1.setUpdatedDate(adminGenericResponseV1.getUpdateDate());
        genericResponseV1.setDateCreated(new Date());
        genericResponseV1.setDateModified(new Date());

        dbMgmtFacade.saveGenericResponseV1(genericResponseV1);
    }

    public void buildAndSaveAdminGenContent(AdminGenericResponseV1 adminGenericResponseV1,
                                            String formIdRef){

        GenericContentManager genericContentManager = new GenericContentManager();

        genericContentManager.setHeading(adminGenericResponseV1.getHeading());
        genericContentManager.setBody(adminGenericResponseV1.getBody());
        genericContentManager.setFormIdRef(formIdRef);
        genericContentManager.setDateCreated(new Date());
        genericContentManager.setDateModified(new Date());

        dbMgmtFacade.saveGenericContentV1(genericContentManager);
    }


    /**
     * Save upcoming data in to respective tables
     */
    public Response buildAndSaveUpcomingData(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO) {

        UpcomingFormsAdminResDTO upcomingFormsAdminResDTO = wrapperGenericAdminResponseV1DTO.getUpcomingFormsAdminResDTO();

        if (Objects.isNull(upcomingFormsAdminResDTO)) {
            webUtils.invalidRequest();
        }

        String ansId = UUIDUtil.generateUUID();

        UpcomingForms upcomingForms = new UpcomingForms();
        upcomingForms.setAppName(upcomingFormsAdminResDTO.getTitle());
        upcomingForms.setId(ansId);
        upcomingForms.setQualification(upcomingFormsAdminResDTO.getQualificationList());
        upcomingForms.setExpDate(upcomingFormsAdminResDTO.getComingDate());
        upcomingForms.setExpVacancy(upcomingFormsAdminResDTO.getExpVacancy());
        upcomingForms.setDateCreated(new Date());
        upcomingForms.setDateModified(new Date());

        dbMgmtFacade.saveUpcomingForms(upcomingForms);

        updateMetaData("UPCOMING");

        return webUtils.buildSuccessResponse("SUCCESS");
    }


    //update resultId in application form
    private void updateResultIdInApplication(String appId, String resultId){
        ApplicationForm applicationForm = dbMgmtFacade.getApplicationForm(appId);
        if(Objects.nonNull(applicationForm)){
            applicationForm.setResultId(resultId);
            dbMgmtFacade.saveApplicationForm(applicationForm);
        }
    }


    /**
     *Fetch All Admin response based on status(PENDING, APPROVED, REJECTED)
     */
    public Response fetchAdminResponseData(String status){
        List<AdminResponseManager> adminResponseManagers = dbMgmtFacade.fetchAdminDataByStatus(status);
        if(CollectionUtils.isEmpty(adminResponseManagers)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.DATA_NOT_FOUND);
        }

        List<AdminResponseDataDTO> responseDataDTOS = adminResponseManagers.stream()
                .map(adminResponseManager -> mapper.convertValue(adminResponseManager, AdminResponseDataDTO.class))
                .toList();

        if(CollectionUtils.isEmpty(responseDataDTOS)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.DATA_NOT_FOUND);
        }
        return Response.ok(responseDataDTOS).build();
    }


    /**
     * Approver approve the final submission of form after review
     * once it will be submitted, respective data will be pushed to db
     * it will also trigger refresh data api.
     * @param approverRequestDTO @mandatory
     * @return
     */
    public Response updateApproverResponse(ApproverRequestDTO approverRequestDTO){
        if(Objects.isNull(approverRequestDTO)){
            return webUtils.invalidRequest();
        }

        AdminResponseManager adminResponseManager = dbMgmtFacade.findAdminResById(approverRequestDTO.getResponseId());

        Response response;

        if(adminResponseManager.getResponseType().equalsIgnoreCase("form")) {
            EnrichedFormDetailsDTO enrichedFormDetailsDTO = new Gson().fromJson(adminResponseManager.getResponse(),
                    EnrichedFormDetailsDTO.class);
            response = pushAppFormToDb(enrichedFormDetailsDTO);

        } else if(adminResponseManager.getResponseType().equalsIgnoreCase("formUpdate")) {
            WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO = new Gson().
                    fromJson(adminResponseManager.getResponse(), WrapperGenericAdminResponseV1DTO.class);
            response = saveFormUpdates(wrapperGenericAdminResponseV1DTO);

        }
        else if (adminResponseManager.getResponseType().equalsIgnoreCase("upcoming")) {
            WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO = new Gson().
                    fromJson(adminResponseManager.getResponse(), WrapperGenericAdminResponseV1DTO.class);
            response = pushUpcomingFormsToDb(wrapperGenericAdminResponseV1DTO);
        } else {
            response = pushAdminGenericResV1ToDb(adminResponseManager);
        }

        if(response != null && response.getStatus() == 200){
            updateAdminResponse(adminResponseManager, approverRequestDTO);
            try {
                responseManagementService.buildAndUpdateClientHomePage();
            }catch (Exception e){
                log.error("Exception occurred while refreshing data : ", e);
            }
        }
        return response;
    }

    private Response saveFormUpdates(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO){

        if(Objects.isNull(wrapperGenericAdminResponseV1DTO) ||
            Objects.isNull(wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1())){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
        }

        AdminGenericResponseV1 adminGenericResponseV1 = wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1();

        ApplicationForm applicationForm = dbMgmtFacade.getApplicationForm(adminGenericResponseV1.getAppIdRef());
        if(Objects.isNull(applicationForm)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
        }

        if(StringUtil.notEmpty(adminGenericResponseV1.getTitle())) {
            applicationForm.setExamName(adminGenericResponseV1.getTitle());
        }
        applicationForm.setDateModified(new Date());
        if(Objects.nonNull(adminGenericResponseV1.getUpdateDate())) {
            applicationForm.setEndDate(adminGenericResponseV1.getUpdateDate());
        }
        dbMgmtFacade.saveApplicationForm(applicationForm);

        if(StringUtil.notEmpty(adminGenericResponseV1.getBody())){
            ApplicationAgeDetails applicationAgeDetails =
                    dbMgmtFacade.getApplicationAgeDetails(adminGenericResponseV1.getAppIdRef());

            if(Objects.isNull(applicationAgeDetails)) return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);;

            if(StringUtil.isEmpty(adminGenericResponseV1.getBody())) return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
            String finalUpdate;
            String update = applicationAgeDetails.getUpdates();
            if(StringUtil.isEmpty(update))
                finalUpdate = adminGenericResponseV1.getBody();
            else
                finalUpdate = update + "<br>" + adminGenericResponseV1.getBody();

            applicationAgeDetails.setUpdates(finalUpdate);
            applicationAgeDetails.setDateModified(new Date());

            dbMgmtFacade.saveApplicationAgeDetail(applicationAgeDetails);
        }

        return webUtils.buildSuccessResponse("SUCCESS");
    }


    /**
     * Push Application form to db from temp table
     * This get trigger when approver approve the app form
     */
    private Response pushAppFormToDb(EnrichedFormDetailsDTO enrichedFormDetailsDTO){

        if(Objects.nonNull(enrichedFormDetailsDTO)){
            return saveFormToDb(enrichedFormDetailsDTO);
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }

    /**
     * Update UPCOMING FORMS Response
     * @param wrapperGenericAdminResponseV1DTO @mandatory
     */
    private Response pushUpcomingFormsToDb(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO){

        if(Objects.nonNull(wrapperGenericAdminResponseV1DTO) &&
                Objects.nonNull(wrapperGenericAdminResponseV1DTO.getUpcomingFormsAdminResDTO())){

            return buildAndSaveUpcomingData(wrapperGenericAdminResponseV1DTO);
        }

        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }

    /**
     * Update (GENERIC) ans key, admit card and result Response
     * @param adminResponseManager @mandatory
     */
    private Response pushAdminGenericResV1ToDb(AdminResponseManager adminResponseManager) {

        WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO = new Gson().fromJson(adminResponseManager.getResponse(),
                WrapperGenericAdminResponseV1DTO.class);

        if (Objects.nonNull(wrapperGenericAdminResponseV1DTO) &&
                Objects.nonNull(wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1())) {

            return buildAndSaveGenericDataV1(wrapperGenericAdminResponseV1DTO,adminResponseManager.getResponseType());
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }


    /**
     * Fetch all form name for search box
     * @return
     */
    public Response fetchAllAppName(){
        List<ApplicationNameDetails> appNameList = dbMgmtFacade.fetchAllAppNames();
        if(CollectionUtils.isEmpty(appNameList)){
            return webUtils.invalidRequest();
        }

        List<ApplicationNameDTO> appNameDTOList = new ArrayList<>();
        for(ApplicationNameDetails appNameDetails : appNameList){
            ApplicationNameDTO appNameDTO = new ApplicationNameDTO();
            appNameDTO.setAppName(appNameDetails.getAppName());
            appNameDTO.setAppIdRef(appNameDetails.getAppIdRef());
            appNameDTO.setPageType(appNameDetails.getAppType());
            appNameDTOList.add(appNameDTO);
        }

        return Response.ok(appNameDTOList).build();
    }


    private AdminResponseManager buildAdminResponse(String  response,
                                                    UserDetails userDetails,
                                                    String type){

        AdminResponseManager adminResponseManager = new AdminResponseManager();

        adminResponseManager.setAdminId(userDetails.getId());
        adminResponseManager.setAdmin_name(userDetails.getFirstName() + " " + userDetails.getLastName());
        adminResponseManager.setResponseType(type);
        adminResponseManager.setResponse(response);
        adminResponseManager.setStatus(StatusEnum.PENDING.name());
        adminResponseManager.setDateCreated(new Date());
        adminResponseManager.setDateModified(new Date());

        return adminResponseManager;
    }

    private void updateAdminResponse(AdminResponseManager adminResponseManager, ApproverRequestDTO approverRequestDTO){
        if(approverRequestDTO.getStatus().equals(StatusEnum.APPROVED.name())){
            adminResponseManager.setResponse(null);
        }
        adminResponseManager.setStatus(approverRequestDTO.getStatus());
        adminResponseManager.setApproverId(approverRequestDTO.getApproverId());
        adminResponseManager.setDateModified(new Date());

        dbMgmtFacade.saveAdminResponse(adminResponseManager);
    }

    public void saveAppNameDetails(String appId, String appName, Date dateCreated, String appType){
        ApplicationNameDetails applicationNameDetails = new ApplicationNameDetails();
        applicationNameDetails.setId(UUIDUtil.generateUUID());
        applicationNameDetails.setAppIdRef(appId);
        applicationNameDetails.setDateCreated(dateCreated);
        applicationNameDetails.setDateModified(dateCreated);
        applicationNameDetails.setAppName(appName);
        applicationNameDetails.setAppType(appType);
        dbMgmtFacade.saveApplicationName(applicationNameDetails);
    }
}