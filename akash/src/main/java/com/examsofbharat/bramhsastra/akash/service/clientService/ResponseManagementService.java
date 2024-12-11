package com.examsofbharat.bramhsastra.akash.service.clientService;

import com.examsofbharat.bramhsastra.akash.constants.AkashConstants;
import com.examsofbharat.bramhsastra.akash.executor.FormExecutorService;
import com.examsofbharat.bramhsastra.akash.utils.*;
import com.examsofbharat.bramhsastra.jal.constants.ErrorConstants;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
import com.examsofbharat.bramhsastra.jal.dto.HomePageCountDataDTO;
import com.examsofbharat.bramhsastra.jal.dto.HomeTotalCountDTO;
import com.examsofbharat.bramhsastra.jal.dto.RelatedFormDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.*;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
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

import static com.examsofbharat.bramhsastra.akash.constants.AkashConstants.*;
import static com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum.*;

@Service
@Slf4j
public class ResponseManagementService {

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    @Autowired
    WebUtils webUtils;

    @Autowired
    ClientService clientService;

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

            sortIndex ++;
        }

        //Update upcoming forms
        updateUpcomingForm(formLandingPageDTO);

        //update home page title and subtitle
        updateHomeTitle(formLandingPageDTO);

        //Update header count
        updateHeaderCountDate(formLandingPageDTO);

        //Update today's count (form and vacancy of today)
        buildAndCacheTodayUpdate(formLandingPageDTO);

        String response = gson.toJson(formLandingPageDTO);
        //push data to cache
        FormUtil.cacheData.put("HOME_PAGE", response);
        pushResponseToDb("HOME_PAGE", response, null);

        //update second page and related form parallel
        buildAndSaveSecAndRelatedData();

        try {
            clientService.updateLatestFormInCache();
        }catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return webUtils.buildSuccessResponse("SUCCESS");
    }

    private void prepareLandingPage(List<ExamMetaData> examMetaDataList, FormTypeEnum formType,
                                    int sortIndex, FormLandingPageDTO formLandingPageDTO){
        List<ExamMetaData> examList = examMetaDataList.stream()
                .filter(examMetaData -> examMetaData.getExamCategory().equalsIgnoreCase(formType.name()))
                .collect(Collectors.toList());

        buildAndParse(examList, formType, sortIndex, formLandingPageDTO);
    }

    private void updateUpcomingForm(FormLandingPageDTO formLandingPageDTO){
        HomeUpcomingFormDTO homeUpcomingFormDTO = new HomeUpcomingFormDTO();

        homeUpcomingFormDTO.setTitle("Upcoming Form");
        homeUpcomingFormDTO.setPageType("UPCOMING");
        homeUpcomingFormDTO.setCardColor(FormUtil.fetchCardColor(1));

        List<UpcomingForms> upcomingFormsList = dbMgmtFacade.getXDaysResponse(2);
        if(!upcomingFormsList.isEmpty()){
            homeUpcomingFormDTO.setLastReleasedCount(String.valueOf(upcomingFormsList.size()));
        }



        formLandingPageDTO.setHomeUpcomingFormDTO(homeUpcomingFormDTO);
    }

    private void updateHomeTitle(FormLandingPageDTO formLandingPageDTO) {
        formLandingPageDTO.setSubTitle(eobInitilizer.getHomeSubtitle());
    }

    private void updateHeaderCountDate(FormLandingPageDTO formLandingPageDTO){
        ResponseManagement responseManagement = dbMgmtFacade.getResponseData("COUNT_UPDATES");
        HomePageCountDataDTO homePageCountDataDTO = new HomePageCountDataDTO();
        HomeTotalCountDTO homeTotalCountDTO;
        if(Objects.nonNull(responseManagement)){
            homeTotalCountDTO = gson.fromJson(responseManagement.getResponse(), HomeTotalCountDTO.class);
            homePageCountDataDTO.setTotalVacancy(FormUtil.formatIntoIndianNumSystem(
                    Integer.parseInt(homeTotalCountDTO.getTotalVacancy())));

            homeTotalCountDTO.setTodayVacancy(homeTotalCountDTO.getTodayVacancy());
            homePageCountDataDTO.setTodayForm(homeTotalCountDTO.getTodayForm());
            homePageCountDataDTO.setTotalForms(homeTotalCountDTO.getTotalForms());

            formLandingPageDTO.setHomePageCountDataDTO(homePageCountDataDTO);
        }
    }

    public Response fetchHomeResponse(String responseType){

        String response = null;
        response = FormUtil.cacheData.get("HOME_PAGE");
        if(StringUtil.isEmpty(response)){
            log.info("Home page not found in cache, calling DB");
            ResponseManagement responseManagement = dbMgmtFacade.getResponseData(responseType);
            response = responseManagement.getResponse();
        }
        if(StringUtil.isEmpty(response)){
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
            if(examMetaData.getTotalForm() < 1)
                continue;
            buildFormSubSections(landingSubSectionDTOList, examMetaData, i);
            i++;
        }

        // Sort by date in descending order using lambda
        landingSubSectionDTOList.sort((e1, e2) -> e2.getSortDate().compareTo(e1.getSortDate())); // Descending order

        landingSectionDTO.setSubSections(landingSubSectionDTOList);
    }

    private void buildHomeAdmitDetails(LandingSectionDTO landingSectionDTO,
                                       FormLandingPageDTO formLandingPageDTO){

        buildGenericV1Sections(landingSectionDTO, ADMIT_KEY, ADMIT_TYPE);
        HomeAdmitCardSection homeAdmitCardSection = new HomeAdmitCardSection();

        //Checking if we have any new admit card in last 2 days
        List<GenericResponseV1> admitCardList = dbMgmtFacade.getLastXDayResponseV1List(2, ADMIT_KEY);
        if(Objects.nonNull(admitCardList)){
            homeAdmitCardSection.setLastAdmitReleaseCount(String.valueOf(admitCardList.size()));
        }

        homeAdmitCardSection.setTitle(FormTypeEnum.ADMIT.getVal());
        homeAdmitCardSection.setType(ADMIT_KEY);
        homeAdmitCardSection.getLandingSectionDTOS().add(landingSectionDTO);
        formLandingPageDTO.getSubPrimeSectionDTO().setHomeAdmitCardSection(homeAdmitCardSection);
    }

    private void buildHomeResultDetails(LandingSectionDTO landingSectionDTO,
                                        FormLandingPageDTO formLandingPageDTO){
        buildGenericV1Sections(landingSectionDTO,RESULT_KEY, RESULT_TYPE);
        HomeResultDetailsDTO homeResultDetailsDTO = new HomeResultDetailsDTO();
        //Checking if we have any new admit card in last 2 days
        List<GenericResponseV1> resultDetailsList = dbMgmtFacade.getLastXDayResponseV1List(2, RESULT_KEY);
        if(Objects.nonNull(resultDetailsList)){
            homeResultDetailsDTO.setLastResultReleaseCount(String.valueOf(resultDetailsList.size()));
        }

        homeResultDetailsDTO.setCardColor(FormUtil.fetchCardColor(1));
        homeResultDetailsDTO.setTitle(FormTypeEnum.RESULT.getVal());
        homeResultDetailsDTO.setType(RESULT_KEY);
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
        List<ApplicationForm> latestFormList = dbMgmtFacade.fetchAllLatestApp(0, 6);

        int i = 0;
        for(ApplicationForm applicationForm : latestFormList){
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();

            landingSubSectionDTO.setExamId(applicationForm.getId());
            landingSubSectionDTO.setUrlTitle(FormUtil.getUrlTitle(FormUtil.getUrlTitle(applicationForm)));
            landingSubSectionDTO.setFormType("form");
            landingSubSectionDTO.setKey(LATEST_FORMS.name());
            landingSubSectionDTO.setTitle(applicationForm.getExamName());
            landingSubSectionDTO.setCardColor(FormUtil.fetchCardColor(i%4));
            landingSubSectionDTO.setLogoUrl(FormUtil.getLogoByName(applicationForm.getExamName()));

            Date fiveDaysAfter = DateUtils.addDays(applicationForm.getDateCreated(), 5);

            //form status banner condition
            if(applicationForm.getDateModified().compareTo(fiveDaysAfter) > 0){
                landingSubSectionDTO.setFormStatus("UPDATES");
            }else if(FormUtil.dateIsWithinXDays(applicationForm.getStartDate())){
                landingSubSectionDTO.setFormStatus("NEW");
            }

            if(applicationForm.getDateModified().compareTo(fiveDaysAfter) > 0){
                landingSubSectionDTO.setShowDateTitle("Updated On");
                landingSubSectionDTO.setShowDateColor("#4A235A");
            }else{
                landingSubSectionDTO.setShowDateTitle("Released On");
                landingSubSectionDTO.setShowDateColor(FormUtil.getLastXDaysDateColor(applicationForm.getDateModified()));
            }

            landingSubSectionDTO.setShowDate(DateUtils.getFormatedDate1(applicationForm.getDateModified()));
            landingSubSectionDTO.setSortDate(applicationForm.getDateModified());
            landingSubSectionDTO.setVacancyTitle("Vacancy");

            String totalVacancy = applicationForm.getTotalVacancy() > 0
                    ? FormUtil.formatIntoIndianNumSystem(applicationForm.getTotalVacancy())
                    : "Not Available";
            landingSubSectionDTO.setTotalVacancy(totalVacancy);

            landingSubSectionDTO.setFormLogoUrl(FormUtil.getLogoByName(applicationForm.getExamName()));
            i++;

            landingSubSectionDTOS.add(landingSubSectionDTO);
        }
        landingSectionDTO.setSubSections(landingSubSectionDTOS);
    }


    private void buildAllOlderForms(LandingSectionDTO landingSectionDTO){
        List<LandingSubSectionDTO> landingSubSectionDTOS = new ArrayList<>();
        List<ApplicationForm> latestFormList = dbMgmtFacade.fetchAllOldestApp(0, 6);

        int i = 0;
        for(ApplicationForm applicationForm : latestFormList){
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();

            landingSubSectionDTO.setExamId(applicationForm.getId());
            landingSubSectionDTO.setFormType("form");
            landingSubSectionDTO.setKey(OLDER_FORMS.name());
            landingSubSectionDTO.setTitle(applicationForm.getExamName());
            landingSubSectionDTO.setCardColor(FormUtil.fetchCardColor(i%4));
            landingSubSectionDTO.setShowDate(DateUtils.getFormatedDate1(applicationForm.getEndDate()));
            landingSubSectionDTO.setSortDate(applicationForm.getDateModified());
            landingSubSectionDTO.setShowDateColor(AkashConstants.RED_COLOR);
            landingSubSectionDTO.setLogoUrl(FormUtil.getLogoByName(applicationForm.getExamName()));
            landingSubSectionDTO.setVacancyTitle("Vacancy");
            landingSubSectionDTO.setTotalVacancy(FormUtil.formatIntoIndianNumSystem(applicationForm.getTotalVacancy()));
            landingSubSectionDTO.setFormLogoUrl(FormUtil.getLogoByName(applicationForm.getExamName()));
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

        colorCardByType(landingSubSectionDTO, i, FormTypeEnum.valueOf(examMetaData.getExamCategory()),
                examMetaData);
        landingSubSectionDTO.setLogoUrl(FormUtil.getLogoByName(examMetaData.getExamSubCategory()));
        landingSubSectionDTO.setKey(FormSubTypeEnum.valueOf(examMetaData.getExamSubCategory()).name());
        landingSubSectionDTO.setTitle(FormSubTypeEnum.valueOf(examMetaData.getExamSubCategory()).getVal());
        landingSubSectionDTO.setFormLogoUrl(FormSubTypeEnum.valueOf(examMetaData.getExamSubCategory()).getVal());
        landingSubSectionDTO.setShowDate(DateUtils.getFormatedDate1(examMetaData.getDateModified()));
        landingSubSectionDTO.setSortDate(examMetaData.getDateModified());
        landingSubSectionDTO.setTotalApplication(examMetaData.getTotalForm());
        landingSubSectionDTO.setVacancyTitle("Total Vacancy");
        landingSubSectionDTO.setTotalVacancy(FormUtil.formatIntoIndianNumSystem(examMetaData.getTotalVacancy()));

        landingSubSectionDTOS.add(landingSubSectionDTO);
    }

    private void colorCardByType(LandingSubSectionDTO landingSubSectionDTO, int i,
                                 FormTypeEnum formType, ExamMetaData examMetaData){
        if(formType.equals(GRADE_BASED)){
            landingSubSectionDTO.setCardColor(FormUtil.fetchNewCardColor(i%4));
            landingSubSectionDTO.setShowDateColor("#FFFF00");
        }else{
            landingSubSectionDTO.setCardColor(FormUtil.fetchCardColor(i%4));
            landingSubSectionDTO.setShowDateColor(FormUtil.getFormShowDateColor(examMetaData.getDateModified()));
        }
    }

    /**
     * build LandingSubSectionDTO for ADMIT, RESULT and ANS KEY (ONE FOR ALL THREE)
     * @param landingSectionDTO @mandatory
     * @param key @mandatory
     * @param formType @mandatory
     */
    public void buildGenericV1Sections(LandingSectionDTO landingSectionDTO, String key, String formType) {

        List<LandingSubSectionDTO> landingSubSectionDTOS = new ArrayList<>();
        List<GenericResponseV1> genericResponse = dbMgmtFacade.getLatestResponseV1List(0,5, AkashConstants.DATE_MODIFIED, key);
        int i = 0;
        for (GenericResponseV1 response : genericResponse) {
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();
            landingSubSectionDTO.setCardColor("#e0f7fa");
            landingSubSectionDTO.setKey(key);
            landingSubSectionDTO.setFormType(formType);
            landingSubSectionDTO.setExamId(response.getId());
            landingSubSectionDTO.setTitle(response.getTitle());
            landingSubSectionDTO.setLogoUrl(FormUtil.getLogoByName(response.getTitle()));
            landingSubSectionDTO.setId(response.getId());
            landingSubSectionDTO.setShowDateColor(FormUtil.getLastXDaysDateColor(response.getUpdatedDate()));
            landingSubSectionDTO.setShowDate(DateUtils.getFormatedDate1(response.getUpdatedDate()));

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
        homeAnsKeySectionDTO.setCardColor(FormUtil.fetchCardColor(0));

        List<String> ansNameList = new ArrayList<>();
        List<GenericResponseV1> ansKeyList = dbMgmtFacade.getLatestResponseV1List(0,5, AkashConstants.DATE_MODIFIED,"ANSKEY");
        ansKeyList.forEach(genericResponseV1 -> ansNameList.add(genericResponseV1.getTitle()));
        homeAnsKeySectionDTO.setResultNameList(ansNameList);

        //Checking if we have any new admit card in last 2 days
        List<GenericResponseV1> answerDetailsList = dbMgmtFacade.getLastXDayResponseV1List(2,"ANSKEY");
        if(Objects.nonNull(answerDetailsList)){
            homeAnsKeySectionDTO.setLastResultReleaseCount(String.valueOf(answerDetailsList.size()));
        }

        homeAnsKeySectionDTO.setUpdatedDate(DateUtils.getFormatedDate1(new Date()));
        formLandingPageDTO.setHomeAnsKeySectionDTO(homeAnsKeySectionDTO);
    }

    private void buildAndCacheTodayUpdate(FormLandingPageDTO formLandingPageDTO){
        String dateType = "dateCreated";
        Date startDate = com.examsofbharat.bramhsastra.prithvi.util.DateUtils.getStartOfDay(new Date());

        List<ApplicationForm> todayData = dbMgmtFacade.getFormAfterDateCreated(0,15, dateType, startDate);
        int newForm = todayData.size();
        int todayVacancy = todayData.stream()
                .mapToInt(ApplicationForm::getTotalVacancy)
                .sum();

        //Update today's count both form and vacancy
        formLandingPageDTO.getHomePageCountDataDTO().setTodayForm(newForm);
        formLandingPageDTO.getHomePageCountDataDTO().setTodayVacancy(todayVacancy);
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
    public void buildAndSaveSecAndRelatedData(){

        int size = eobInitilizer.getSecPageItemCount();
        for(FormSubTypeEnum formSubTypeEnum : FormSubTypeEnum.values()){
            if(formSubTypeEnum.equals(FormSubTypeEnum.STATE)){
                //will think latter based on response because to save each state it will have huge data in response table
                continue;
            }
            //should be sync call, populating map for caching data
            fetchBuildAndSaveSecAndRelatedData(formSubTypeEnum.name(), size);
        }

        //save and cache latest data, keep sync call
        fetchBuildAndSaveSecAndRelatedData(LATEST_FORMS.name(), size);
        //save and cache older data, keep sync call
        fetchBuildAndSaveSecAndRelatedData(OLDER_FORMS.name(), size);

        //save upcoming forms
        fetchAndCacheUpcomingForms();
    }

    private void fetchBuildAndSaveSecAndRelatedData(String formSubType, int size){
        List<RelatedFormDTO> relatedForms = new ArrayList<>();
        String responseRes = applicationDbUtil.fetchSecDataAndRelatedData(formSubType, 0, size, relatedForms);
        FormUtil.cacheData.put(0 + "_" + formSubType , responseRes);
        buildRelatedFormAndSaveResponse(responseRes, relatedForms, formSubType);
    }

    private void fetchAndCacheUpcomingForms(){

        String responseRes = applicationDbUtil.buildSecDataForUpcomingForm(0, 10);
        FormUtil.cacheData.put(0 + "_" + "UPCOMING", responseRes);
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
