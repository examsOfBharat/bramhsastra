package com.examsofbharat.bramhsastra.akash.service.adminService;

import com.examsofbharat.bramhsastra.akash.service.EmailService;
import com.examsofbharat.bramhsastra.akash.service.clientService.ResponseManagementService;
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

import java.io.FileNotFoundException;
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

    ObjectMapper mapper = new ObjectMapper();

    public Response formLandingResponse(){
        return Response.ok().build();
    }

    public Response saveFormPDF(EnrichedFormDetailsDTO enrichedFormDetailsDTO) throws FileNotFoundException, IllegalAccessException {
        byte[] response = PdfGeneratorUtil.generateFormPdf(enrichedFormDetailsDTO);
        log.info("PDF Generated successfully!");
        String to = "bibhu.bhushan0403@gmail.com";
        String subject = "ExamsOfBharat New Form Submitted";
        String htmlBody = "<html><body><h1>Hello!</h1><p>This is a test email with <b>bold</b> and <i>italic</i> text.</p></body></html>";
        String attachmentName = enrichedFormDetailsDTO.getApplicationFormDTO().getId()+".pdf";
        emailService.sendEmailWithPDFAttachment(to, subject, htmlBody, response, attachmentName);

        return Response.ok().build();
    }

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
            buildFormPdfAndSendMail(enrichedFormDetailsDTO, userDetails);

            return webUtils.buildSuccessResponse(WebConstants.SUCCESS);
        }catch (Exception e){
            log.info("Exception occurred while submitting form",e);
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }

    /**
     * Once admin submit admit card, put it in temporary table
     * send mail for review along with pdf page
     * @param wrapperGenericAdminResponseV1DTO @mandatory
     */
    public Response processAndSaveAdminAdmitResponse(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO){
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

            AdminResponseManager adminResponseManager = buildAdminResponse(response, userDetails, "admit");

            dbMgmtFacade.saveAdminResponse(adminResponseManager);

            buildAdmitPdfAndSendMail(wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1(), userDetails);
            return Response.ok().build();
        }catch (Exception e){
            log.info("Exception occurred while submitting form",e);
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }

    /**
     * Once result details, put it in temporary table
     * send mail for review along with pdf page
     * @param wrapperGenericAdminResponseV1DTO @mandatory
     */
    public Response processAndSaveAdminResultResponse(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO){
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

            AdminResponseManager adminResponseManager = buildAdminResponse(response, userDetails, "result");

            dbMgmtFacade.saveAdminResponse(adminResponseManager);

            buildResultPdfAndSendMail(wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1(), userDetails);
            return Response.ok().build();
        }catch (Exception e){
            log.info("Exception occurred while submitting form",e);
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }



    /**
     * Once ans key details, put it in temporary table
     * send mail for review along with pdf page
     * @param wrapperGenericAdminResponseV1DTO @mandatory
     */
    public Response processAndSaveGenericResV1(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO, String pageType){
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

            AdminResponseManager adminResponseManager = buildAdminResponse(response, userDetails, pageType);

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
    public Response saveForm(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
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
        dbMgmtFacade.saveApplicationUrl(mapper.convertValue(applicationUrlsDTO, ApplicationUrl.class));


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

    /**
     * Once reviewer will confirm will push data into main table from temporary table
     * Save admit card detail
     */
    public Response saveAdmitCard(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO){

        AdminGenericResponseV1 admitCardResponse = wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1();
        if(Objects.isNull(admitCardResponse)){
            return webUtils.invalidRequest();
        }
        String admitCardId = UUIDUtil.generateUUID();

        GenericResponseV1 genericResponseV1 = new GenericResponseV1();
        genericResponseV1.setId(admitCardId);
        genericResponseV1.setTitle(admitCardResponse.getTitle());
        genericResponseV1.setShowDate(admitCardResponse.getShowDate());
        genericResponseV1.setAppIdRef(admitCardResponse.getAppIdRef());
        genericResponseV1.setDownloadUrl(admitCardResponse.getDownloadUrl());
        genericResponseV1.setUpdatedDate(admitCardResponse.getUpdateDate());
        genericResponseV1.setType("ADMIT");
        genericResponseV1.setDateCreated(new Date());
        genericResponseV1.setDateModified(new Date());

        dbMgmtFacade.saveGenericResponseV1(genericResponseV1);

        //save seo detail
        if(Objects.nonNull(wrapperGenericAdminResponseV1DTO.getApplicationSeoDetailsDTO())) {
            saveSeoDetails(wrapperGenericAdminResponseV1DTO.getApplicationSeoDetailsDTO(), admitCardId);
        }

        //save admit card details
        saveAppNameDetails(admitCardId, admitCardResponse.getTitle(), new Date(), "admit");

        //save admit card content details
        buildAndSaveAdmitContent(admitCardId, admitCardResponse);

        //update admitId in applicationForm
        if(StringUtil.notEmpty(admitCardResponse.getAppIdRef())) {
            updateAdmitIdInApplication(admitCardResponse.getAppIdRef(), admitCardId);
        }

        //Update meta-data for admit card
        updateMetaData("ADMIT");

        responseManagementService.buildAndUpdateClientHomePage();

        return webUtils.buildSuccessResponse("SUCCESS");
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
     * Save admit card content details
     */
    private void buildAndSaveAdmitContent(String admitIdRef, AdminGenericResponseV1 admitResponse){

        GenericContentManager genericContentManager = new GenericContentManager();

        genericContentManager.setFormIdRef(admitIdRef);
        genericContentManager.setHeading(admitResponse.getHeading());
        genericContentManager.setBody(admitResponse.getBody());
        genericContentManager.setAddOn(admitResponse.getAddOn());
        genericContentManager.setDateCreated(new Date());
        genericContentManager.setDateModified(new Date());

        dbMgmtFacade.saveGenericContentV1(genericContentManager);

    }


    /**
     * Save result data in to respective tables
     */
    public Response buildAndSaveResultData(WrapperGenericAdminResponseV1DTO wrapperResultRequestDTO){

        AdminGenericResponseV1 resultResponse = wrapperResultRequestDTO.getAdminGenericResponseV1();

        if(Objects.isNull(resultResponse)){
            webUtils.invalidRequest();
        }

        String resultId = UUIDUtil.generateUUID();

        GenericResponseV1 genericResponseV1 = new GenericResponseV1();
        genericResponseV1.setId(resultId);
        genericResponseV1.setAppIdRef(resultResponse.getAppIdRef());
        genericResponseV1.setTitle(resultResponse.getTitle());
        genericResponseV1.setUpdatedDate(resultResponse.getUpdateDate());
        genericResponseV1.setDownloadUrl(resultResponse.getDownloadUrl());
        genericResponseV1.setType("RESULT");
        genericResponseV1.setDateCreated(new Date());
        genericResponseV1.setDateModified(new Date());

        dbMgmtFacade.saveGenericResponseV1(genericResponseV1);

        if(Objects.nonNull(wrapperResultRequestDTO.getApplicationSeoDetailsDTO())){
            saveSeoDetails(wrapperResultRequestDTO.getApplicationSeoDetailsDTO(), resultId);
        }

        saveAppNameDetails(resultId, resultResponse.getTitle(), new Date(), "result");

        saveResultContent(resultResponse, resultId);

        //update admitId in applicationForm
        updateResultIdInApplication(resultResponse.getAppIdRef(), resultId);

        return webUtils.buildSuccessResponse("SUCCESS");
    }


    /**
     * Save ans key data in to respective tables
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

        saveAppNameDetails(formId, adminGenericResponseV1.getTitle(), new Date(), pageType);


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
        upcomingForms.setDateCreated(new Date());
        upcomingForms.setDateModified(new Date());
        upcomingForms.setFormDate(upcomingFormsAdminResDTO.getComingDate());

        dbMgmtFacade.saveUpcomingForms(upcomingForms);

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
     * Save result content detail
     */
    public void saveResultContent(AdminGenericResponseV1 resultResponse, String resultId){

        GenericContentManager genericContentManager = new GenericContentManager();
        genericContentManager.setFormIdRef(resultId);
        genericContentManager.setHeading(resultResponse.getHeading());
        genericContentManager.setBody(resultResponse.getBody());
        genericContentManager.setAddOn(resultResponse.getAddOn());
        genericContentManager.setDateCreated(new Date());
        genericContentManager.setDateModified(new Date());

        dbMgmtFacade.saveGenericContentV1(genericContentManager);
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

    public Response updateApproverResponse(ApproverRequestDTO approverRequestDTO){
        if(Objects.isNull(approverRequestDTO)){
            return webUtils.invalidRequest();
        }

        AdminResponseManager adminResponseManager = dbMgmtFacade.findAdminResById(approverRequestDTO.getResponseId());

        if(adminResponseManager.getResponseType().equalsIgnoreCase("form")) {
            EnrichedFormDetailsDTO enrichedFormDetailsDTO = new Gson().fromJson(adminResponseManager.getResponse(),
                    EnrichedFormDetailsDTO.class);
            return updateFormResponse(enrichedFormDetailsDTO, adminResponseManager, approverRequestDTO);

        } else if (adminResponseManager.getResponseType().equalsIgnoreCase("upcoming")) {
            WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO = new Gson().
                    fromJson(adminResponseManager.getResponse(), WrapperGenericAdminResponseV1DTO.class);
            return updateUpcomingResponse(wrapperGenericAdminResponseV1DTO, adminResponseManager, approverRequestDTO);
        } else {
            return updateAdminGenericResV1(adminResponseManager, approverRequestDTO);
        }
    }

    private Response updateFormResponse(EnrichedFormDetailsDTO enrichedFormDetailsDTO,
                                        AdminResponseManager adminResponseManager,
                                        ApproverRequestDTO approverRequestDTO){
        if(Objects.nonNull(enrichedFormDetailsDTO)){
            Response response = saveForm(enrichedFormDetailsDTO);
            if(response != null && response.getStatus() == 200){
                updateAdminResponse(adminResponseManager, approverRequestDTO);
            }
            return response;
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }

    private Response updateAdmitResponse(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO,
                                         AdminResponseManager adminResponseManager,
                                         ApproverRequestDTO approverRequestDTO){
        if(Objects.nonNull(wrapperGenericAdminResponseV1DTO) &&
        Objects.nonNull(wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1())){
            Response response = saveAdmitCard(wrapperGenericAdminResponseV1DTO);
            if(response != null && response.getStatus() == 200){
                updateAdminResponse(adminResponseManager, approverRequestDTO);
            }
            return response;
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }

    private Response updateResultResponse(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO,
                                          AdminResponseManager adminResponseManager,
                                          ApproverRequestDTO approverRequestDTO){

        if(Objects.nonNull(wrapperGenericAdminResponseV1DTO) &&
                Objects.nonNull(wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1())){

            Response response = buildAndSaveResultData(wrapperGenericAdminResponseV1DTO);
            if(response != null && response.getStatus() == 200){
                updateAdminResponse(adminResponseManager, approverRequestDTO);
            }
            return response;
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }

    /**
     * Update ANS KEY Response
     * @param adminResponseManager @mandatory
     * @param approverRequestDTO @mandatory
     */
    private Response updateAdminGenericResV1(AdminResponseManager adminResponseManager,
                                             ApproverRequestDTO approverRequestDTO) {


        WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO = new Gson().fromJson(adminResponseManager.getResponse(),
                WrapperGenericAdminResponseV1DTO.class);

        if (Objects.nonNull(wrapperGenericAdminResponseV1DTO) &&
                Objects.nonNull(wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1())) {

            Response response = buildAndSaveGenericDataV1(wrapperGenericAdminResponseV1DTO,adminResponseManager.getResponseType());
            if (response != null && response.getStatus() == 200) {
                updateAdminResponse(adminResponseManager, approverRequestDTO);
            }
            return response;
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }

    /**
     * Update UPCOMING FORMS Response
     * @param wrapperGenericAdminResponseV1DTO @mandatory
     * @param adminResponseManager @mandatory
     * @param approverRequestDTO @mandatory
     * @return
     */
    private Response updateUpcomingResponse(WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO,
                                          AdminResponseManager adminResponseManager,
                                          ApproverRequestDTO approverRequestDTO){

        if(Objects.nonNull(wrapperGenericAdminResponseV1DTO) &&
                Objects.nonNull(wrapperGenericAdminResponseV1DTO.getUpcomingFormsAdminResDTO())){

            Response response = buildAndSaveUpcomingData(wrapperGenericAdminResponseV1DTO);
            if(response != null && response.getStatus() == 200){
                updateAdminResponse(adminResponseManager, approverRequestDTO);
            }
            return response;
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }


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

    private void buildResultPdfAndSendMail(AdminGenericResponseV1 resultResponse, UserDetails userDetails)  {
        byte[] pdfBytes = new byte[0];
        try {
            pdfBytes = PdfGeneratorUtil.generateResultPdf(resultResponse, userDetails);
        }catch (Exception e){
            log.info("Exception occurred while generating admit pdf");
        }
        log.info("PDF generated");

        String to = "bibhu.bhushan0403@gmail.com";
        String subject = "ExamsOfBharat admin form submitted";
        String body = FormUtil.buildEmailHtml("Approver", userDetails.getFirstName(),
                resultResponse.getTitle());
        String attachmentName = DateUtils.getDateFileName("result", "pdf");
        emailService.sendEmailWithPDFAttachment(to, subject, body, pdfBytes, attachmentName);
    }

    private void buildAdmitPdfAndSendMail(AdminGenericResponseV1 admitCardRequestDTO, UserDetails userDetails)  {
        byte[] pdfBytes = new byte[0];
        try {
            pdfBytes = PdfGeneratorUtil.generatePdf(admitCardRequestDTO);
        }catch (Exception e){
            log.info("Exception occurred while generating admit pdf");
        }
        log.info("PDF generated");

        String to = "bibhu.bhushan0403@gmail.com";
        String subject = "ExamsOfBharat admin form submitted";
        String body = FormUtil.buildEmailHtml("Approver", userDetails.getFirstName(),
                admitCardRequestDTO.getTitle());
        String attachmentName = DateUtils.getDateFileName("admit", "pdf");
        emailService.sendEmailWithPDFAttachment(to, subject, body, pdfBytes, attachmentName);
    }

    private void buildFormPdfAndSendMail(EnrichedFormDetailsDTO enrichedFormDetailsDTO, UserDetails userDetails)  {
        byte[] pdfBytes = PdfGeneratorUtil.generateFormPdfDoc(enrichedFormDetailsDTO);
        log.info("PDF generated");

        String to = "bibhu.bhushan0403@gmail.com";
        String subject = "ExamsOfBharat admin form submitted";
        String body = FormUtil.buildEmailHtml("Approver", userDetails.getFirstName(),
                enrichedFormDetailsDTO.getApplicationFormDTO().getExamName());
        String attachmentName = DateUtils.getDateFileName("form", "pdf");
        emailService.sendEmailWithPDFAttachment(to, subject, body, pdfBytes, attachmentName);

        String to1 = userDetails.getEmailId();
        String subject1 = "ExamsOfBharat admin form submitted";
        String body1 = FormUtil.buildEmailHtml("Admin", userDetails.getFirstName(),
                enrichedFormDetailsDTO.getApplicationFormDTO().getExamName());
        String attachmentName1 = DateUtils.getDateFileName("form", "pdf");
        emailService.sendEmailWithPDFAttachment(to1, subject1, body1, pdfBytes, attachmentName1);
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