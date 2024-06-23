package com.examsofbharat.bramhsastra.akash.service;

import com.examsofbharat.bramhsastra.akash.utils.UUIDUtil;
import com.examsofbharat.bramhsastra.akash.utils.WebUtils;
import com.examsofbharat.bramhsastra.akash.validator.FormValidator;
import com.examsofbharat.bramhsastra.jal.dto.*;
import com.examsofbharat.bramhsastra.jal.dto.request.AdmitCardRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.ResultRequestDTO;
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

    ObjectMapper mapper = new ObjectMapper();

    public Response formLandingResponse(){
        return Response.ok().build();
    }

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

    public void updateHomeCount(int totalVacancy){
        ResponseManagement responseManagement;
        responseManagement = dbMgmtFacade.getResponseData("COUNT_UPDATES");
        HomePageCountDataDTO homePageCountDataDTO;
        if(Objects.nonNull(responseManagement)){
            homePageCountDataDTO = new Gson().fromJson(responseManagement.getResponse(), HomePageCountDataDTO.class);
            homePageCountDataDTO.setTotalForms(homePageCountDataDTO.getTotalForms() + 1);
            homePageCountDataDTO.setTotalVacancy(homePageCountDataDTO.getTotalVacancy() + totalVacancy);
            String response = new Gson().toJson(homePageCountDataDTO);
            responseManagement.setResponse(response);
            responseManagement.setDateModified(new Date());
            dbMgmtFacade.saveResponseData(responseManagement);
        }

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


}