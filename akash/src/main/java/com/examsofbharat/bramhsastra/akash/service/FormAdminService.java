package com.examsofbharat.bramhsastra.akash.service;

import com.examsofbharat.bramhsastra.akash.utils.*;
import com.examsofbharat.bramhsastra.akash.validator.FormValidator;
import com.examsofbharat.bramhsastra.jal.constants.ErrorConstants;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
import com.examsofbharat.bramhsastra.jal.dto.*;
import com.examsofbharat.bramhsastra.jal.dto.request.*;
import com.examsofbharat.bramhsastra.jal.dto.response.AdminResponseDataDTO;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.StatusEnum;
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
     * @param wrapperAdmitCardRequestDTO @mandatory
     */
    public Response processAndSaveAdminAdmitResponse(WrapperAdmitCardRequestDTO wrapperAdmitCardRequestDTO){
        if(Objects.isNull(wrapperAdmitCardRequestDTO) ||
                Objects.isNull(wrapperAdmitCardRequestDTO.getAdminUserDetailsDTO()) ||
                Objects.isNull(wrapperAdmitCardRequestDTO.getAdmitCardRequestDTO())){
            log.info("Invalid save admit card request");
            return webUtils.invalidRequest();
        }

        try{
            String response = new Gson().toJson(wrapperAdmitCardRequestDTO.getAdmitCardRequestDTO());

            UserDetails userDetails = dbMgmtFacade.findUserByUserId(wrapperAdmitCardRequestDTO.
                    getAdminUserDetailsDTO().getUserId());

            AdminResponseManager adminResponseManager = buildAdminResponse(response, userDetails, "admit");

            dbMgmtFacade.saveAdminResponse(adminResponseManager);

            buildAdmitPdfAndSendMail(wrapperAdmitCardRequestDTO.getAdmitCardRequestDTO(), userDetails);
            return Response.ok().build();
        }catch (Exception e){
            log.info("Exception occurred while submitting form",e);
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }

    /**
     * Once result details, put it in temporary table
     * send mail for review along with pdf page
     * @param wrapperResultRequestDTO @mandatory
     */
    public Response processAndSaveAdminResultResponse(WrapperResultRequestDTO wrapperResultRequestDTO){
        if(Objects.isNull(wrapperResultRequestDTO) ||
                Objects.isNull(wrapperResultRequestDTO.getAdminUserDetailsDTO()) ||
                Objects.isNull(wrapperResultRequestDTO.getResultRequestDTO())){

            log.info("Invalid save admit card request");
            return webUtils.invalidRequest();
        }

        try{
            String response = new Gson().toJson(wrapperResultRequestDTO.getResultRequestDTO());

            UserDetails userDetails = dbMgmtFacade.findUserByUserId(wrapperResultRequestDTO.
                    getAdminUserDetailsDTO().getUserId());

            AdminResponseManager adminResponseManager = buildAdminResponse(response, userDetails, "result");

            dbMgmtFacade.saveAdminResponse(adminResponseManager);

            buildResultPdfAndSendMail(wrapperResultRequestDTO.getResultRequestDTO(), userDetails);
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

        ApplicationFeeDTO applicationFeeDTO = enrichedFormDetailsDTO.getApplicationFeeDTO();
        applicationFeeDTO.setId(UUIDUtil.generateUUID());
        applicationFeeDTO.setAppIdRef(examId);
        applicationFeeDTO.setDateCreated(dateCreated);
        applicationFeeDTO.setDateModified(dateCreated);
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

        if(applicationVacancyDTOList != null && !applicationVacancyDTOList.isEmpty()){
            applicationVacancyDTOList.forEach(formVacancyDTO -> {
                formVacancyDTO.setId(UUIDUtil.generateUUID());
                formVacancyDTO.setAppIdRef(examId);
                formVacancyDTO.setDateCreated(dateCreated);
                formVacancyDTO.setDateModified(dateCreated);

                vacancyDetailList.add(mapper.convertValue(formVacancyDTO, ApplicationVacancyDetails.class));
            });
            dbMgmtFacade.saveApplicationVacancyDetail(vacancyDetailList);
        }

        List<ApplicationEligibilityDTO> eligibilityDetails = enrichedFormDetailsDTO.getApplicationEligibilityDTOS();
        List<ApplicationEligibilityDetails> eligibilityDetailsList = new ArrayList<>();
        if(eligibilityDetails != null && !eligibilityDetails.isEmpty()){
            eligibilityDetails.forEach(eligibilityDetailsDTO -> {
                eligibilityDetailsDTO.setId(UUIDUtil.generateUUID());
                eligibilityDetailsDTO.setAppIdRef(examId);
                eligibilityDetailsDTO.setDateCreated(dateCreated);
                eligibilityDetailsDTO.setDateModified(dateCreated);

                eligibilityDetailsList.add(mapper.convertValue(eligibilityDetailsDTO, ApplicationEligibilityDetails.class));
            });
            dbMgmtFacade.saveEligibilityDetails(eligibilityDetailsList);
        }

        //save application name and type
        saveAppNameDetails(examId, applicationFormDTO.getExamName(),dateCreated, "form");

        //save and update application meta-data
        saveOrUpdateApplicationMetaData(applicationFormDTO, applicationVacancyDTOList);

        return Response.ok().build();
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
            examMetaData.setTotalVacancy(examMetaData.getTotalVacancy() + applicationFormDTO.getTotalVacancy());
            examMetaData.setTotalForm(examMetaData.getTotalForm()+1);
            examMetaData.setDateModified(new Date());
            dbMgmtFacade.saveExamMetaData(examMetaData);
        }

        //Update Home page data
        updateHomeCount(applicationFormDTO.getTotalVacancy());

        //once we get new data, will update landing page response to reflect updated data
        responseManagementService.buildAndUpdateClientHomePage();
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
     * Save admit card detail
     */
    public Response saveAdmitCard(AdmitCardRequestDTO admitCardRequestDTO){
        if(Objects.isNull(admitCardRequestDTO)){
            return webUtils.invalidRequest();
        }
        String admitCardId = UUIDUtil.generateUUID();

        AdmitCard admitCard = new AdmitCard();
        admitCard.setId(admitCardId);
        admitCard.setAdmitCardName(admitCardRequestDTO.getAdmitCardName());
        admitCard.setExamDate(admitCardRequestDTO.getExamDate());
        admitCard.setAppIdRef(admitCardRequestDTO.getAppIdRef());
        admitCard.setDownloadUrl(admitCardRequestDTO.getDownloadUrl());
        admitCard.setDateCreated(new Date());
        admitCard.setDateModified(new Date());

        dbMgmtFacade.saveAdmitCard(admitCard);

        //save admit card details
        saveAppNameDetails(admitCardId, admitCard.getAdmitCardName(), new Date(), "admit");

        //save admit card content details
        buildAndSaveAdmitContent(admitCardId, admitCardRequestDTO);

        //update admitId in applicationForm
        updateAdmitIdInApplication(admitCardRequestDTO.getAppIdRef(), admitCardId);

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
    private void buildAndSaveAdmitContent(String admitIdRef, AdmitCardRequestDTO admitCardRequestDTO){

        AdmitContentManager admitContentManager = new AdmitContentManager();
        admitContentManager.setAdmitIdRef(admitIdRef);
        admitContentManager.setHeading(admitCardRequestDTO.getHeading());
        admitContentManager.setBody(admitCardRequestDTO.getBody());
        admitContentManager.setAddOn(admitCardRequestDTO.getAddOn());
        admitContentManager.setDateCreated(new Date());
        admitContentManager.setDateModified(new Date());

        dbMgmtFacade.saveAdmitContent(admitContentManager);
    }


    /**
     * Save result data in to respective tables
     */
    public Response buildAndSaveResultData(ResultRequestDTO resultRequestDTO){

        if(Objects.isNull(resultRequestDTO)){
            webUtils.invalidRequest();
        }

        String resultId = UUIDUtil.generateUUID();
        ResultDetails resultDetails = new ResultDetails();
        resultDetails.setId(resultId);
        resultDetails.setAppIdRef(resultRequestDTO.getAppIdRef());
        resultDetails.setResultName(resultRequestDTO.getResultName());
        resultDetails.setResultDate(resultRequestDTO.getResultDate());
        resultDetails.setResultUrl(resultRequestDTO.getResultUrl());
        resultDetails.setDateCreated(new Date());
        resultDetails.setDateModified(new Date());

        dbMgmtFacade.saveResultDetail(resultDetails);

        saveAppNameDetails(resultId, resultDetails.getResultName(), new Date(), "result");

        saveResultContent(resultRequestDTO, resultId);

        //update admitId in applicationForm
        updateResultIdInApplication(resultRequestDTO.getAppIdRef(), resultId);

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
    public void saveResultContent(ResultRequestDTO resultRequestDTO, String resultId){

        ResultContentManager resultContentManager = new ResultContentManager();
        resultContentManager.setResultIdRef(resultId);
        resultContentManager.setHeading(resultRequestDTO.getHeading());
        resultContentManager.setBody(resultRequestDTO.getBody());
        resultContentManager.setAddOn(resultRequestDTO.getAddOn());
        resultContentManager.setDateCreated(new Date());
        resultContentManager.setDateModified(new Date());

        dbMgmtFacade.saveResultContent(resultContentManager);
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
        } else if (adminResponseManager.getResponseType().equalsIgnoreCase("admit")) {
            AdmitCardRequestDTO admitCardRequestDTO = new Gson().fromJson(adminResponseManager.getResponse(),
                    AdmitCardRequestDTO.class);
            return updateAdmitResponse(admitCardRequestDTO, adminResponseManager, approverRequestDTO);
        } else if (adminResponseManager.getResponseType().equalsIgnoreCase("result")) {
            ResultRequestDTO resultRequestDTO = new Gson().fromJson(adminResponseManager.getResponse(),
                    ResultRequestDTO.class);
            return updateResultResponse(resultRequestDTO, adminResponseManager, approverRequestDTO);
        }

        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
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

    private Response updateAdmitResponse(AdmitCardRequestDTO admitCardRequestDTO,
                                         AdminResponseManager adminResponseManager,
                                         ApproverRequestDTO approverRequestDTO){
        if(Objects.nonNull(admitCardRequestDTO)){
            Response response = saveAdmitCard(admitCardRequestDTO);
            if(response != null && response.getStatus() == 200){
                updateAdminResponse(adminResponseManager, approverRequestDTO);
            }
            return response;
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.SERVER_ERROR);
    }

    private Response updateResultResponse(ResultRequestDTO resultRequestDTO,
                                          AdminResponseManager adminResponseManager,
                                          ApproverRequestDTO approverRequestDTO){
        if(Objects.nonNull(resultRequestDTO)){
            Response response = buildAndSaveResultData(resultRequestDTO);
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

    private void buildResultPdfAndSendMail(ResultRequestDTO resultRequestDTO, UserDetails userDetails)  {
        byte[] pdfBytes = new byte[0];
        try {
            pdfBytes = PdfGeneratorUtil.generateResultPdf(resultRequestDTO, userDetails);
        }catch (Exception e){
            log.info("Exception occurred while generating admit pdf");
        }
        log.info("PDF generated");

        String to = "bibhu.bhushan0403@gmail.com";
        String subject = "ExamsOfBharat admin form submitted";
        String body = FormUtil.buildEmailHtml("Approver", userDetails.getFirstName(),
                resultRequestDTO.getResultName());
        String attachmentName = DateUtils.getDateFileName("result", "pdf");
        emailService.sendEmailWithPDFAttachment(to, subject, body, pdfBytes, attachmentName);
    }

    private void buildAdmitPdfAndSendMail(AdmitCardRequestDTO admitCardRequestDTO, UserDetails userDetails)  {
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
                admitCardRequestDTO.getAdmitCardName());
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