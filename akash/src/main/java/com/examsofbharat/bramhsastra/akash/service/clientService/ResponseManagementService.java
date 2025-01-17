package com.examsofbharat.bramhsastra.akash.service.clientService;

import com.examsofbharat.bramhsastra.akash.constants.AkashConstants;
import com.examsofbharat.bramhsastra.akash.executor.FormExecutorService;
import com.examsofbharat.bramhsastra.akash.processors.blogProcessor.ProcessBlogResponse;
import com.examsofbharat.bramhsastra.akash.utils.*;
import com.examsofbharat.bramhsastra.akash.utils.mapper.MapperUtils;
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

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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

    @Autowired
    ProcessBlogResponse processBlogResponse;

    private static final Gson gson = new Gson();


    public Response buildAndUpdateClientHomePage() {
        List<ExamMetaData> examMetaDataList = dbMgmtFacade.getExamMetaData();

        FormLandingPageDTO formLandingPageDTO = initializeFormLandingPageDTO();

        int sortIndex = 1;
        for (FormTypeEnum formType : FormTypeEnum.values()) {
            List<ExamMetaData> examList = filterExamMetaDataByCategory(examMetaDataList, formType);
            buildAndParse(examList, formType, sortIndex++, formLandingPageDTO);
        }

        updateUpcomingForm(formLandingPageDTO);
        processBlogResponse.processHomeBlog(formLandingPageDTO);
        updateHomeTitle(formLandingPageDTO);
        updateHeaderCountDate(formLandingPageDTO);
        buildAndCacheTodayUpdate(formLandingPageDTO);

        String response = gson.toJson(formLandingPageDTO);
        FormUtil.cacheData.put("HOME_PAGE", response);
        pushResponseToDb("HOME_PAGE", response, null);

        buildAndSaveSecAndRelatedData();

        executeAsyncTasks();

        return webUtils.buildSuccessResponse("SUCCESS");
    }

    public Response refreshIndividualSection(String formType){

        String response = FormUtil.cacheData.get("HOME_PAGE");
        if(StringUtil.isEmpty(response)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.DATA_NOT_FOUND);
        }

        FormLandingPageDTO formLandingPageDTO = gson.fromJson(response, FormLandingPageDTO.class);
        if(Objects.isNull(formLandingPageDTO)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.MAPPING_ERROR);
        }

        List<ExamMetaData> examMetaDataList = dbMgmtFacade.getExamMetaData();

        List<ExamMetaData> examList = examMetaDataList.stream()
                .filter(examMetaData -> examMetaData.getExamCategory().equalsIgnoreCase(formType))
                .collect(Collectors.toList());

        buildAndParse(examList, FormTypeEnum.valueOf(formType), 0, formLandingPageDTO);

        return webUtils.buildSuccessResponse("SUCCESS");
    }

    //Refresh HomeUpdate section
    public Response refreshUpdateSection(){

        String response = FormUtil.cacheData.get("HOME_PAGE");
        if(StringUtil.isEmpty(response)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.DATA_NOT_FOUND);
        }

        FormLandingPageDTO formLandingPageDTO = gson.fromJson(response, FormLandingPageDTO.class);
        if(Objects.isNull(formLandingPageDTO)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.MAPPING_ERROR);
        }

        buildHomeUpdatesDetails(formLandingPageDTO);
        String finalResponse = gson.toJson(formLandingPageDTO);
        //push data to cache
        FormUtil.cacheData.put("HOME_PAGE", finalResponse);

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

        switch (formTypeEnum){
            case OLDER_FORMS -> buildHomeOlderData(formLandingPageDTO, formTypeEnum);
            case LATEST_FORMS -> buildHomeLatestData(formLandingPageDTO);
            case ADMIT -> buildHomeAdmitDetails(formTypeEnum,formLandingPageDTO, examMetaDataList);
            case RESULT -> buildHomeResultDetails(formTypeEnum,formLandingPageDTO, examMetaDataList);
            case UPDATES -> buildHomeUpdatesDetails(formLandingPageDTO);
            case ANS_KEY -> buildHomeAnsKeyDetails(formLandingPageDTO, examMetaDataList);
            case GRADE_BASED -> buildHomeGradeSection(formLandingPageDTO,formTypeEnum, examMetaDataList);
            case PROVINCIAL_BASED -> buildHomeProvince(formLandingPageDTO,formTypeEnum, examMetaDataList);
            default ->  buildOtherFormDetails(formLandingPageDTO, examMetaDataList, formTypeEnum);
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

    private void buildHomeAdmitDetails(FormTypeEnum formTypeEnum,
                                       FormLandingPageDTO formLandingPageDTO, List<ExamMetaData> examMetaDataList){

        LandingSectionDTO landingSectionDTO = FormUtil.populateBasicLandingSection(formTypeEnum);
        buildGenericV1Sections(landingSectionDTO, ADMIT_KEY, ADMIT_TYPE);
        HomeAdmitCardSection homeAdmitCardSection = new HomeAdmitCardSection();

        //Checking if we have any new admit card in last 2 days
        List<GenericResponseV1> admitCardList = dbMgmtFacade.getLastXDayResponseV1List(2, ADMIT_KEY);
        if(Objects.nonNull(admitCardList)){
            homeAdmitCardSection.setLastAdmitReleaseCount(String.valueOf(admitCardList.size()));
        }
        if(!CollectionUtils.isEmpty(examMetaDataList)){
            homeAdmitCardSection.setTotalApplication(examMetaDataList.get(0).getTotalForm());
        }
        homeAdmitCardSection.setTitle(FormTypeEnum.ADMIT.getVal());
        homeAdmitCardSection.setType(ADMIT_KEY);
        homeAdmitCardSection.getLandingSectionDTOS().add(landingSectionDTO);
        formLandingPageDTO.getSubPrimeSectionDTO().setHomeAdmitCardSection(homeAdmitCardSection);
    }

    private void buildHomeResultDetails(FormTypeEnum formTypeEnum,
                                        FormLandingPageDTO formLandingPageDTO,
                                        List<ExamMetaData> examMetaDataList){

        LandingSectionDTO landingSectionDTO = FormUtil.populateBasicLandingSection(formTypeEnum);
        buildGenericV1Sections(landingSectionDTO,RESULT_KEY, RESULT_TYPE);
        HomeResultDetailsDTO homeResultDetailsDTO = new HomeResultDetailsDTO();
        //Checking if we have any new admit card in last 2 days
        List<GenericResponseV1> resultDetailsList = dbMgmtFacade.getLastXDayResponseV1List(2, RESULT_KEY);
        if(Objects.nonNull(resultDetailsList)){
            homeResultDetailsDTO.setLastResultReleaseCount(String.valueOf(resultDetailsList.size()));
        }
        if(!CollectionUtils.isEmpty(examMetaDataList)){
            homeResultDetailsDTO.setTotalApplication(examMetaDataList.get(0).getTotalForm());
        }
        homeResultDetailsDTO.setCardColor(FormUtil.fetchCardColor(1));
        homeResultDetailsDTO.setTitle(FormTypeEnum.RESULT.getVal());
        homeResultDetailsDTO.setType(RESULT_KEY);
        homeResultDetailsDTO.getLandingSectionDTOS().add(landingSectionDTO);
        formLandingPageDTO.getSubPrimeSectionDTO().setHomeResultDetailsDTO(homeResultDetailsDTO);
    }

    private void buildHomeLatestData(FormLandingPageDTO formLandingPageDTO){

        buildLatestAndPopularForm(formLandingPageDTO);
    }

    private void buildHomeOlderData(FormLandingPageDTO formLandingPageDTO, FormTypeEnum formTypeEnum){

        LandingSectionDTO landingSectionDTO = FormUtil.populateBasicLandingSection(formTypeEnum);
        buildAllOlderForms(landingSectionDTO);
        formLandingPageDTO.getSuperPrimeSectionDTO().getLandingSectionDTOS().add(landingSectionDTO);
    }

    private void buildHomeProvince(FormLandingPageDTO formLandingPageDTO,
                                 FormTypeEnum formTypeEnum,
                                   List<ExamMetaData> examMetaDataList){

        LandingSectionDTO landingSectionDTO = FormUtil.populateBasicLandingSection(formTypeEnum);

        buildSubSection(examMetaDataList, landingSectionDTO);
        HomeProvinceSectionDTO homeProvinceSectionDTO = new HomeProvinceSectionDTO();

        homeProvinceSectionDTO.getLandingSectionDTOS().add(landingSectionDTO);
        homeProvinceSectionDTO.setSubtitle("Hey! Now you can check form based on different region(Stage/central)");

        formLandingPageDTO.setHomeProvinceSectionDTO(homeProvinceSectionDTO);
    }

    private void buildOtherFormDetails(FormLandingPageDTO formLandingPageDTO,
                                       List<ExamMetaData> examMetaDataList, FormTypeEnum formTypeEnum){

        LandingSectionDTO landingSectionDTO = FormUtil.populateBasicLandingSection(formTypeEnum);
        buildSubSection(examMetaDataList, landingSectionDTO);
        formLandingPageDTO.getPrimeSectionDTO().getLandingSectionDTOS().add(landingSectionDTO);

    }

    private void buildHomeGradeSection(FormLandingPageDTO formLandingPageDTO,
                                       FormTypeEnum formTypeEnum,
                                       List<ExamMetaData> examMetaDataList){

        LandingSectionDTO landingSectionDTO = FormUtil.populateBasicLandingSection(formTypeEnum);

        buildSubSection(examMetaDataList, landingSectionDTO);
        HomeGradeSectionDTO homeGradeSectionDTO = new HomeGradeSectionDTO();

        homeGradeSectionDTO.getLandingSectionDTOS().add(landingSectionDTO);
        homeGradeSectionDTO.setSubtitle("Hey! Good News, now check your forms by JOB GRADE");
        homeGradeSectionDTO.setImgUrl(EobInitilizer.getHomeBgUrl());

        formLandingPageDTO.setHomeGradeSectionDTO(homeGradeSectionDTO);
    }

    private void buildLatestAndPopularForm(FormLandingPageDTO formLandingPageDTO){

        List<ApplicationForm> latestFormList = dbMgmtFacade.fetchAllLatestApp(0, 10);

        LandingSectionDTO latestLandingSectionDTO = FormUtil.populateBasicLandingSection(LATEST_FORMS);
        buildAllLatestForms(latestLandingSectionDTO, LATEST_FORMS.name(), latestFormList.subList(0,5), formLandingPageDTO);

        List<ApplicationForm> popularFormList = applicationDbUtil.filterPopularForms(latestFormList);
        int pageCount = 1;
        while(popularFormList.size() < 5 || !CollectionUtils.isEmpty(latestFormList)){
            latestFormList = dbMgmtFacade.fetchAllLatestApp(pageCount, 10);
            pageCount++;
            popularFormList.addAll(applicationDbUtil.filterPopularForms(latestFormList));
        }
        LandingSectionDTO popularLandingSectionDTO = FormUtil.populateBasicLandingSection(POPULAR_FORMS);
        buildAllLatestForms(popularLandingSectionDTO, POPULAR_FORMS.name(), popularFormList.subList(0,5), formLandingPageDTO);

    }

    private void buildAllLatestForms(LandingSectionDTO landingSectionDTO, String formType,
                                     List<ApplicationForm> latestFormList, FormLandingPageDTO formLandingPageDTO) {

        List<LandingSubSectionDTO> landingSubSectionDTOS = latestFormList.stream().map(applicationForm -> {
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();

            landingSubSectionDTO.setExamId(applicationForm.getId());
            landingSubSectionDTO.setUrlTitle(FormUtil.getUrlTitle(applicationForm));
            landingSubSectionDTO.setFormType("form");
            landingSubSectionDTO.setKey(formType);
            landingSubSectionDTO.setTitle(applicationForm.getExamName());
            landingSubSectionDTO.setCardColor(FormUtil.fetchCardColor(latestFormList.indexOf(applicationForm) % 4));
            landingSubSectionDTO.setLogoUrl(FormUtil.getLogoByName(applicationForm.getExamName()));

            Date fiveDaysAfter = DateUtils.addDays(applicationForm.getDateCreated(), 5);

            if (applicationForm.getDateModified().compareTo(fiveDaysAfter) > 0) {
                landingSubSectionDTO.setFormStatus("UPDATES");
                landingSubSectionDTO.setShowDateTitle("Updated On");
                landingSubSectionDTO.setShowDateColor("#4A235A");
            } else if (FormUtil.dateIsWithinXDays(applicationForm.getStartDate())) {
                landingSubSectionDTO.setFormStatus("NEW");
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

            return landingSubSectionDTO;
        }).collect(Collectors.toList());

        landingSectionDTO.setSubSections(landingSubSectionDTOS);
        formLandingPageDTO.getSuperPrimeSectionDTO().getLandingSectionDTOS().add(landingSectionDTO);
    }

    private void buildAllOlderForms(LandingSectionDTO landingSectionDTO){
        List<LandingSubSectionDTO> landingSubSectionDTOS = new ArrayList<>();
        List<ApplicationForm> latestFormList = dbMgmtFacade.fetchAllOldestApp(0, 5);

        int i = 0;
        for(ApplicationForm applicationForm : latestFormList){
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();

            landingSubSectionDTO.setExamId(applicationForm.getId());
            landingSubSectionDTO.setFormType("form");
            landingSubSectionDTO.setKey(OLDER_FORMS.name());
            landingSubSectionDTO.setTitle(applicationForm.getExamName());
            landingSubSectionDTO.setUrlTitle(FormUtil.getUrlTitle(applicationForm));
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

    private void buildHomeAnsKeyDetails(FormLandingPageDTO formLandingPageDTO, List<ExamMetaData> examMetaDataList) {
        HomeAnsKeySectionDTO homeAnsKeySectionDTO = createHomeAnsKeySectionDTO();

        List<String> ansNameList = fetchLatestAnsKeyNames();
        homeAnsKeySectionDTO.setResultNameList(ansNameList);

        setTotalApplicationsIfPresent(homeAnsKeySectionDTO, examMetaDataList);

        setLastResultReleaseCount(homeAnsKeySectionDTO);

        homeAnsKeySectionDTO.setUpdatedDate(DateUtils.getFormatedDate1(new Date()));
        formLandingPageDTO.setHomeAnsKeySectionDTO(homeAnsKeySectionDTO);
    }

    private HomeAnsKeySectionDTO createHomeAnsKeySectionDTO() {
        HomeAnsKeySectionDTO homeAnsKeySectionDTO = new HomeAnsKeySectionDTO();
        homeAnsKeySectionDTO.setTitle(FormTypeEnum.ANS_KEY.getVal());
        homeAnsKeySectionDTO.setAnsKeyType(ANS_KEY.name());
        homeAnsKeySectionDTO.setSubTitle("Click view all to check all answer key");
        homeAnsKeySectionDTO.setCardColor(FormUtil.fetchCardColor(0));
        return homeAnsKeySectionDTO;
    }

    private List<String> fetchLatestAnsKeyNames() {
        return dbMgmtFacade.getLatestResponseV1List(0, 5, AkashConstants.DATE_MODIFIED, "ANSKEY")
                .stream()
                .map(GenericResponseV1::getTitle)
                .collect(Collectors.toList());
    }

    private void setTotalApplicationsIfPresent(HomeAnsKeySectionDTO homeAnsKeySectionDTO, List<ExamMetaData> examMetaDataList) {
        if (!CollectionUtils.isEmpty(examMetaDataList)) {
            homeAnsKeySectionDTO.setTotalApplication(examMetaDataList.get(0).getTotalForm());
        }
    }

    private void setLastResultReleaseCount(HomeAnsKeySectionDTO homeAnsKeySectionDTO) {
        List<GenericResponseV1> answerDetailsList = dbMgmtFacade.getLastXDayResponseV1List(2, "ANSKEY");
        if (Objects.nonNull(answerDetailsList)) {
            homeAnsKeySectionDTO.setLastResultReleaseCount(String.valueOf(answerDetailsList.size()));
        }
    }

    //prepare update section for form
    private void buildHomeUpdatesDetails(FormLandingPageDTO formLandingPageDTO){
        UpdatesSectionDTO updatesSectionDTO = new UpdatesSectionDTO();

        List<GenericResponseV1DTO> updateSectionDTOList =
                dbMgmtFacade.getLatestResponseV1List(0, 10, AkashConstants.DATE_MODIFIED, "UPDATES")
                        .stream()
                        .map(MapperUtils::toUpdateSectionDTO) // Map each entity to DTO
                        .toList();
        updatesSectionDTO.setUpdateList(updateSectionDTOList);
        updatesSectionDTO.setTitle(UPDATES.getVal());

        formLandingPageDTO.setUpdatesSectionDTO(updatesSectionDTO);
    }

    private void buildAndCacheTodayUpdate(FormLandingPageDTO formLandingPageDTO) {
        String dateType = "dateCreated";
        Date startDate = DateUtils.getStartOfDay(new Date());

        List<ApplicationForm> todayData = dbMgmtFacade.getFormAfterDateCreated(0, 15, dateType, startDate);
        int newFormCount = todayData.size();
        int todayVacancyCount = todayData.stream()
                .mapToInt(ApplicationForm::getTotalVacancy)
                .sum();

        HomePageCountDataDTO homePageCountData = formLandingPageDTO.getHomePageCountDataDTO();
        homePageCountData.setTodayForm(newFormCount);
        homePageCountData.setTodayVacancy(todayVacancyCount);
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
    public void buildAndSaveSecAndRelatedData() {
        int size = eobInitilizer.getSecPageItemCount();
        Arrays.stream(FormSubTypeEnum.values())
                .filter(formSubTypeEnum -> !formSubTypeEnum.equals(FormSubTypeEnum.STATE))
                .forEach(formSubTypeEnum -> fetchBuildAndSaveSecAndRelatedData(formSubTypeEnum.name(), size));

        fetchBuildAndSaveSecAndRelatedData(LATEST_FORMS.name(), size);
        fetchBuildAndSaveSecAndRelatedData(OLDER_FORMS.name(), size);

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

    private FormLandingPageDTO initializeFormLandingPageDTO() {
        FormLandingPageDTO formLandingPageDTO = new FormLandingPageDTO();
        formLandingPageDTO.setSuperPrimeSectionDTO(new SuperPrimeSectionDTO());
        formLandingPageDTO.setSubPrimeSectionDTO(new SubPrimeSectionDTO());
        formLandingPageDTO.setPrimeSectionDTO(new PrimeSectionDTO());
        return formLandingPageDTO;
    }

    private List<ExamMetaData> filterExamMetaDataByCategory(List<ExamMetaData> examMetaDataList, FormTypeEnum formType) {
        return examMetaDataList.stream()
                .filter(examMetaData -> examMetaData.getExamCategory().equalsIgnoreCase(formType.name()))
                .collect(Collectors.toList());
    }

    private void executeAsyncTasks() {
        try {
            Future<?> future = FormExecutorService.genResCacheService.submit(clientService::updateLatestFormInCache);
            Future<?> future1 = FormExecutorService.genResCacheService.submit(clientService::cacheGenResponse);

            future.get();
            future1.get();

            log.info("******Loading data has been completed! *******");
        } catch (ExecutionException | InterruptedException | CancellationException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
