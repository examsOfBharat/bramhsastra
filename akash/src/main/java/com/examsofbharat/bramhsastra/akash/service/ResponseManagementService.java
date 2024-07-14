package com.examsofbharat.bramhsastra.akash.service;

import com.examsofbharat.bramhsastra.akash.constants.AkashConstants;
import com.examsofbharat.bramhsastra.akash.executor.FormExecutorService;
import com.examsofbharat.bramhsastra.akash.utils.*;
import com.examsofbharat.bramhsastra.jal.constants.ErrorConstants;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
import com.examsofbharat.bramhsastra.jal.dto.HomePageCountDataDTO;
import com.examsofbharat.bramhsastra.jal.dto.RelatedFormDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.*;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.examsofbharat.bramhsastra.prithvi.entity.*;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import com.google.gson.Gson;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum.*;

@Service
@Slf4j
public class ResponseManagementService {

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    @Autowired
    WebUtils webUtils;

    @Autowired
    ApplicationDbUtil applicationDbUtil;

    @Autowired
    private EobInitilizer eobInitilizer;

    private static final Gson gson = new Gson();

    public Response buildAndUpdateClientHomePage() {
        List<ExamMetaData> examMetaDataList = dbMgmtFacade.getExamMetaData();

        FormLandingPageDTO formLandingPageDTO = new FormLandingPageDTO();
        SuperPrimeSectionDTO superPrimeSectionDTO =  new SuperPrimeSectionDTO();
        SubPrimeSectionDTO subPrimeSectionDTO = new SubPrimeSectionDTO();
        PrimeSectionDTO primeSectionDTO = new PrimeSectionDTO();

        formLandingPageDTO.setSuperPrimeSectionDTO(superPrimeSectionDTO);
        formLandingPageDTO.setSubPrimeSectionDTO(subPrimeSectionDTO);
        formLandingPageDTO.setPrimeSectionDTO(primeSectionDTO);

        //TODO shortIndex should be configurable (please try to configure from db)
        
        int sortIndex = 1;
        for (FormTypeEnum formType : FormTypeEnum.values()) {
            List<ExamMetaData> examList = examMetaDataList.stream()
                    .filter(examMetaData -> examMetaData.getExamCategory().equalsIgnoreCase(formType.name()))
                    .collect(Collectors.toList());

           buildAndParse(examList, formType, sortIndex, formLandingPageDTO);
            sortIndex++;
        }
//        formLandingPageDTO.setLandingSectionDTOS(landingSectionDTOS);

        //update home page title and subtitle
        updateHomeTitle(formLandingPageDTO);

        //Update header count
        updateHeaderCountDate(formLandingPageDTO);

        String response = gson.toJson(formLandingPageDTO);
        pushResponseToDb("HOME_PAGE", response, null);

        buildAndSaveResponseToDb();

        return webUtils.buildSuccessResponse("SUCCESS");
    }

    private void updateHomeTitle(FormLandingPageDTO formLandingPageDTO) {
        formLandingPageDTO.setEngTitle(eobInitilizer.getHomeEngTitle());
        formLandingPageDTO.setHindiTitle(eobInitilizer.getHomeHindiTitle());
        formLandingPageDTO.setSubTitle(eobInitilizer.getHomeSubtitle());
    }

    private void updateHeaderCountDate(FormLandingPageDTO formLandingPageDTO){
        ResponseManagement responseManagement = dbMgmtFacade.getResponseData("COUNT_UPDATES");
        HomePageCountDataDTO homePageCountDataDTO;
        if(Objects.nonNull(responseManagement)){
            homePageCountDataDTO = gson.fromJson(responseManagement.getResponse(), HomePageCountDataDTO.class);
            formLandingPageDTO.setHomePageCountDataDTO(homePageCountDataDTO);
        }
    }

    public Response fetchHomeResponse(String responseType){
        ResponseManagement responseManagement = dbMgmtFacade.getResponseData(responseType);
        if(Objects.isNull(responseManagement)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.DATA_NOT_FOUND);
        }

        FormLandingPageDTO response = null;
        try {
            response = gson.fromJson(responseManagement.getResponse(), FormLandingPageDTO.class);
        }catch (Exception e){
             log.error("Exception occurred while parsing homeResponse responseType :: {}", responseType);
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.DATA_NOT_FOUND);
        }

        if(Objects.isNull(response)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.DATA_NOT_FOUND);
        }

        return Response.ok(response).build();
    }

    public void buildAndParse(List<ExamMetaData> examMetaDataList,
                                           FormTypeEnum formTypeEnum,
                                           int sortIndex,
                                           FormLandingPageDTO formLandingPageDTO) {

        boolean viewAll = (formTypeEnum.equals(FormTypeEnum.ADMIT) ||
                formTypeEnum.equals(FormTypeEnum.RESULT) ||
                formTypeEnum.equals(LATEST_FORMS) ||
                formTypeEnum.equals(OLDER_FORMS));

        LandingSectionDTO landingSectionDTO = buildSections(sortIndex, formTypeEnum, viewAll);

        switch (formTypeEnum){
            case OLDER_FORMS -> buildHomeOlderData(landingSectionDTO, formLandingPageDTO);
            case LATEST_FORMS -> buildHomeLatestData(landingSectionDTO, formLandingPageDTO);
            case ADMIT -> buildHomeAdmitDetails(landingSectionDTO, formLandingPageDTO);
            case RESULT -> buildHomeResultDetails(landingSectionDTO, formLandingPageDTO);
            case ANS_KEY -> buildHomeAnsKeyDetails(formLandingPageDTO);
            case GRADE_BASED -> buildHomeGradeSection(formLandingPageDTO, landingSectionDTO, examMetaDataList);
            case PROVINCIAL_BASED -> buildHomeProvince(formLandingPageDTO, landingSectionDTO, examMetaDataList);
            default ->  buildOtherFormDetails(formLandingPageDTO,landingSectionDTO, examMetaDataList);
        }
    }


    private void buildSubSection(List<ExamMetaData> examMetaDataList, LandingSectionDTO landingSectionDTO) {
        List<LandingSubSectionDTO> landingSubSectionDTOList = new ArrayList<>();
        int i = 0;
        for (ExamMetaData examMetaData : examMetaDataList) {
            buildFormSubSections(landingSubSectionDTOList, examMetaData, i);
            i++;
        }
        landingSectionDTO.setSubSections(landingSubSectionDTOList);
    }

    private void buildHomeAdmitDetails(LandingSectionDTO landingSectionDTO,
                                                    FormLandingPageDTO formLandingPageDTO){
        buildAdmitSubSections(landingSectionDTO);
        HomeAdmitCardSection homeAdmitCardSection = new HomeAdmitCardSection();
        homeAdmitCardSection.setLastUpdate(DateUtils.getFormatedDate1(new Date()));
        homeAdmitCardSection.setUpdateDateColor(FormUtil.getLastXDaysDateColor(new Date()));
        homeAdmitCardSection.setLastAdmitReleaseCount("4");
        homeAdmitCardSection.setCardColor(FormUtil.fetchSecCardColor(0));
        homeAdmitCardSection.setLastReleaseCountTitle("Admit card released in last 2 days : ");
        homeAdmitCardSection.setTitle(FormTypeEnum.ADMIT.getVal());
        homeAdmitCardSection.setType("ADMIT");
        homeAdmitCardSection.getLandingSectionDTOS().add(landingSectionDTO);
        formLandingPageDTO.getSubPrimeSectionDTO().setHomeAdmitCardSection(homeAdmitCardSection);
    }

    private void buildHomeResultDetails(LandingSectionDTO landingSectionDTO,
                                                     FormLandingPageDTO formLandingPageDTO){
        buildResultSubSection(landingSectionDTO);
        HomeResultDetailsDTO homeResultDetailsDTO = new HomeResultDetailsDTO();
        homeResultDetailsDTO.setLastUpdate(DateUtils.getFormatedDate1(new Date()));
        homeResultDetailsDTO.setLastResultReleaseCount("4");
        homeResultDetailsDTO.setUpdateDateColor(FormUtil.getLastXDaysDateColor(new Date()));
        homeResultDetailsDTO.setCardColor(FormUtil.fetchSecCardColor(2));
        homeResultDetailsDTO.setLastReleaseCountTitle("Result released in last 2 days : ");
        homeResultDetailsDTO.setTitle(FormTypeEnum.RESULT.getVal());
        homeResultDetailsDTO.setType("RESULT");
        homeResultDetailsDTO.getLandingSectionDTOS().add(landingSectionDTO);
        formLandingPageDTO.getSubPrimeSectionDTO().setHomeResultDetailsDTO(homeResultDetailsDTO);
    }

    private void buildHomeLatestData(LandingSectionDTO landingSectionDTO,
                                                  FormLandingPageDTO formLandingPageDTO){
        buildAllLatestForms(landingSectionDTO);
        formLandingPageDTO.getSuperPrimeSectionDTO().getLandingSectionDTOS().add(landingSectionDTO);
    }

    private void buildHomeOlderData(LandingSectionDTO landingSectionDTO,
                                                 FormLandingPageDTO formLandingPageDTO){
        buildAllOlderForms(landingSectionDTO);
        formLandingPageDTO.getSuperPrimeSectionDTO().getLandingSectionDTOS().add(landingSectionDTO);
    }

    private void buildHomeProvince(FormLandingPageDTO formLandingPageDTO,
                                 LandingSectionDTO landingSectionDTO,
                                   List<ExamMetaData> examMetaDataList){

        buildSubSection(examMetaDataList, landingSectionDTO);
        HomeProvinceSectionDTO homeProvinceSectionDTO = new HomeProvinceSectionDTO();

        homeProvinceSectionDTO.getLandingSectionDTOS().add(landingSectionDTO);
        homeProvinceSectionDTO.setSubtitle("Hey! Now you can check form based on different region(Stage/central)");

        formLandingPageDTO.setHomeProvinceSectionDTO(homeProvinceSectionDTO);
    }

    private void buildOtherFormDetails(FormLandingPageDTO formLandingPageDTO,
                                       LandingSectionDTO landingSectionDTO,
                                       List<ExamMetaData> examMetaDataList){

        buildSubSection(examMetaDataList, landingSectionDTO);
        formLandingPageDTO.getPrimeSectionDTO().getLandingSectionDTOS().add(landingSectionDTO);

    }

    private void buildHomeGradeSection(FormLandingPageDTO formLandingPageDTO,
                                       LandingSectionDTO landingSectionDTO,
                                       List<ExamMetaData> examMetaDataList){

        buildSubSection(examMetaDataList, landingSectionDTO);

        HomeGradeSectionDTO homeGradeSectionDTO = new HomeGradeSectionDTO();

        homeGradeSectionDTO.getLandingSectionDTOS().add(landingSectionDTO);
        homeGradeSectionDTO.setSubtitle("Hey! Good News, now check your forms by JOB GRADE");
        homeGradeSectionDTO.setImgUrl(EobInitilizer.getHomeBgUrl());

        formLandingPageDTO.setHomeGradeSectionDTO(homeGradeSectionDTO);
    }

    private void buildAllLatestForms(LandingSectionDTO landingSectionDTO){

        List<LandingSubSectionDTO> landingSubSectionDTOS = new ArrayList<>();
        List<ApplicationForm> latestFormList = dbMgmtFacade.fetchAllLatestApp(0, 10);

        int i = 0;
        for(ApplicationForm applicationForm : latestFormList){
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();
            landingSubSectionDTO.setKey(LATEST_FORMS.name());
            landingSubSectionDTO.setTitle(applicationForm.getExamName());
            landingSubSectionDTO.setCardColor(FormUtil.fetchCardColor(i%4));
            landingSubSectionDTO.setShowDate(DateUtils.getFormatedDate1(applicationForm.getStartDate()));
            landingSubSectionDTO.setShowDateColor(FormUtil.getLastXDaysDateColor(applicationForm.getStartDate()));
            landingSubSectionDTO.setTotalVacancy(applicationForm.getTotalVacancy());
            i++;

            landingSubSectionDTOS.add(landingSubSectionDTO);
        }
        landingSectionDTO.setSubSections(landingSubSectionDTOS);

    }
    private void buildAllOlderForms(LandingSectionDTO landingSectionDTO){
        List<LandingSubSectionDTO> landingSubSectionDTOS = new ArrayList<>();
        List<ApplicationForm> latestFormList = dbMgmtFacade.fetchAllOldestApp(0, 10);

        int i = 0;
        for(ApplicationForm applicationForm : latestFormList){
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();
            landingSubSectionDTO.setKey(OLDER_FORMS.name());
            landingSubSectionDTO.setTitle(applicationForm.getExamName());
            landingSubSectionDTO.setCardColor(FormUtil.fetchCardColor(i%4));
            landingSubSectionDTO.setShowDate(DateUtils.getFormatedDate1(applicationForm.getEndDate()));
            landingSubSectionDTO.setShowDateColor(AkashConstants.RED_COLOR);
            landingSubSectionDTO.setTotalVacancy(applicationForm.getTotalVacancy());
            i++;

            landingSubSectionDTOS.add(landingSubSectionDTO);
        }
        landingSectionDTO.setSubSections(landingSubSectionDTOS);
    }


    public LandingSectionDTO buildSections(int sortIndex, FormTypeEnum formTypeEnum, boolean viewALl) {

        LandingSectionDTO landingSectionDTO = new LandingSectionDTO();
        landingSectionDTO.setType(formTypeEnum);
        landingSectionDTO.setTitle(formTypeEnum.getVal());
        landingSectionDTO.setSortIndex(sortIndex);
        landingSectionDTO.setViewAll(viewALl);

        return landingSectionDTO;
    }

    public void buildFormSubSections(List<LandingSubSectionDTO> landingSubSectionDTOS, ExamMetaData examMetaData, int i) {

        LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();

        landingSubSectionDTO.setCardColor(FormUtil.fetchCardColor(i%4));
        landingSubSectionDTO.setKey(FormSubTypeEnum.valueOf(examMetaData.getExamSubCategory()).name());
        landingSubSectionDTO.setTitle(FormSubTypeEnum.valueOf(examMetaData.getExamSubCategory()).getVal());
        landingSubSectionDTO.setShowDate(DateUtils.getFormatedDate1(examMetaData.getDateModified()));
        landingSubSectionDTO.setShowDateColor(FormUtil.getFormShowDateColor(examMetaData.getDateModified()));
        landingSubSectionDTO.setTotalApplication(examMetaData.getTotalForm());
        landingSubSectionDTO.setTotalVacancy(examMetaData.getTotalVacancy());

        landingSubSectionDTOS.add(landingSubSectionDTO);
    }

    //build tox x admit card list to send to home page
    public void buildAdmitSubSections(LandingSectionDTO landingSectionDTO) {

        List<LandingSubSectionDTO> landingSubSectionDTOS = new ArrayList<>();
        List<AdmitCard> admitCardList = dbMgmtFacade.getLatestAdmitCardList(0,6, AkashConstants.DATE_CREATED);
        int i = 0;
        for (AdmitCard admitCard : admitCardList) {
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();
//            landingSubSectionDTO.setCardColor(FormUtil.fetchCardColor(i%4));
            landingSubSectionDTO.setCardColor("#e0f7fa");
            landingSubSectionDTO.setKey("ADMIT");
            landingSubSectionDTO.setTitle(admitCard.getAdmitCardName());
            landingSubSectionDTO.setExamDate(FormUtil.formatExamDate(admitCard.getExamDate()));
            landingSubSectionDTO.setId(admitCard.getId());
            landingSubSectionDTO.setExamId(admitCard.getAppIdRef());
            landingSubSectionDTO.setExamDateColor(FormUtil.getLastXDaysDateColor(admitCard.getExamDate()));
            landingSubSectionDTO.setShowDate(DateUtils.getFormatedDate1(admitCard.getDateCreated()));

            landingSubSectionDTOS.add(landingSubSectionDTO);
            i++;
        }
        landingSectionDTO.setSubSections(landingSubSectionDTOS);
    }

    private void buildHomeAnsKeyDetails(FormLandingPageDTO formLandingPageDTO){
        HomeAnsKeySectionDTO homeAnsKeySectionDTO = new HomeAnsKeySectionDTO();
        homeAnsKeySectionDTO.setTitle(FormTypeEnum.ANS_KEY.getVal());
        homeAnsKeySectionDTO.setAnsKeyType(ANS_KEY.name());
        homeAnsKeySectionDTO.setSubTitle("Click view all to check all answer key");

        List<String> ansNameList = new ArrayList<>();

        List<ApplicationForm> latestAnsKey = dbMgmtFacade.getFormWithAnsKey(0,5);

        latestAnsKey.forEach(applicationForm -> ansNameList.add(applicationForm.getExamName()));
        homeAnsKeySectionDTO.setResultNameList(ansNameList);

        formLandingPageDTO.setHomeAnsKeySectionDTO(homeAnsKeySectionDTO);
    }

    //build tox x result list to send to home page
    public void buildResultSubSection(LandingSectionDTO landingSectionDTO) {

        List<LandingSubSectionDTO> landingSubSectionDTOS = new ArrayList<>();
        //fetch result list
        List<ResultDetails> resultDetailsList = dbMgmtFacade.getResultDetailList(0,6, AkashConstants.DATE_CREATED);

        int i = 0;
        for (ResultDetails resultDetails : resultDetailsList) {
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();

//            landingSubSectionDTO.setCardColor(FormUtil.fetchCardColor(i%4));
            landingSubSectionDTO.setCardColor("#e8f5e9");
            landingSubSectionDTO.setKey("RESULT");
            landingSubSectionDTO.setTitle(resultDetails.getResultName());
            landingSubSectionDTO.setShowDate(DateUtils.getFormatedDate1(resultDetails.getResultDate()));
            landingSubSectionDTO.setExamId(resultDetails.getAppIdRef());
            landingSubSectionDTO.setId(resultDetails.getId());
            landingSubSectionDTO.setShowDateColor(FormUtil.getLastXDaysDateColor(resultDetails.getResultDate()));

            landingSubSectionDTOS.add(landingSubSectionDTO);
            i++;
        }
        landingSectionDTO.setSubSections(landingSubSectionDTOS);

    }

    public void saveResponseToDb(String responseType, String response, String relatedResponse) {
        FormExecutorService.responseSaveService.submit(()->
                pushResponseToDb(responseType, response, relatedResponse));
    }

    public void pushResponseToDb(String responseType, String response, String relatedResponse){
        ResponseManagement responseManagement = dbMgmtFacade.getResponseData(responseType);
        if(Objects.isNull(responseManagement)){
            responseManagement = new ResponseManagement();
            responseManagement.setDateCreated(new Date());
        }

        responseManagement.setResponseType(responseType);
        responseManagement.setResponse(response);
        responseManagement.setRelatedForm(relatedResponse);
        responseManagement.setDateModified(new Date());

        dbMgmtFacade.saveResponseData(responseManagement);
    }


    //build secondary page response based on type and subType
    public void buildAndSaveResponseToDb(){

        for(FormSubTypeEnum formSubTypeEnum : FormSubTypeEnum.values()){
            if(formSubTypeEnum.equals(FormSubTypeEnum.STATE)){
                //will think latter based on response because to save each state it will have huge data in response table
                continue;
            }
            List<RelatedFormDTO> relatedForms = new ArrayList<>();
            String responseRes = applicationDbUtil.fetchResponseBasedOnSubType(formSubTypeEnum.name(), 0, 10, relatedForms);
            buildRelatedFormAndSaveResponse(responseRes, relatedForms, formSubTypeEnum.name());
        }

        List<RelatedFormDTO> relatedForms = new ArrayList<>();
        String responseRes = applicationDbUtil.fetchResponseBasedOnSubType(LATEST_FORMS.name(), 0, 10, relatedForms);
        buildRelatedFormAndSaveResponse(responseRes, relatedForms, LATEST_FORMS.name());

        List<RelatedFormDTO> relatedForms2 = new ArrayList<>();
        String responseRes2 = applicationDbUtil.fetchResponseBasedOnSubType(OLDER_FORMS.name(), 0, 10, relatedForms);
        buildRelatedFormAndSaveResponse(responseRes2, relatedForms2, OLDER_FORMS.name());

    }

    private void buildRelatedFormAndSaveResponse(String responseRes,
                                                 List<RelatedFormDTO> relatedForms,
                                                 String formSubType){
        if(CollectionUtils.isEmpty(relatedForms)){
            relatedForms = null;
        }
        RelatedFormResponseDTO relatedFormResponseDTO = new RelatedFormResponseDTO();
        relatedFormResponseDTO.setRelatedFormDTOList(relatedForms);
        String relatedResponse = gson.toJson(relatedFormResponseDTO);

        saveResponseToDb(formSubType, responseRes, relatedResponse);
    }
}
