package com.examsofbharat.bramhsastra.akash.service.clientService;

import com.examsofbharat.bramhsastra.akash.executor.FormExecutorService;
import com.examsofbharat.bramhsastra.akash.factory.componentParser.FormViwerFactory;
import com.examsofbharat.bramhsastra.akash.utils.DateUtils;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.akash.utils.UUIDUtil;
import com.examsofbharat.bramhsastra.akash.utils.WebUtils;
import com.examsofbharat.bramhsastra.akash.validator.FormValidator;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
import com.examsofbharat.bramhsastra.jal.dto.*;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EligibilityCheckRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.*;
import com.examsofbharat.bramhsastra.jal.enums.ComponentEnum;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.entity.*;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.examsofbharat.bramhsastra.jal.constants.ErrorConstants.DATA_NOT_FOUND;

@Slf4j
@Service
public class ClientService {
    @Autowired
    DBMgmtFacade dbMgmtFacade;

    @Autowired
    WebUtils webUtils;

    @Autowired
    FormViwerFactory formViwerFactory;

    ObjectMapper objectMapper = new ObjectMapper();


    public Response buildAndGetApplication(String appId){

        //fetch form detail from db
        EnrichedFormDetailsDTO enrichedFormDetailsDTO = getEnrichedFormDetails(appId);
        if(Objects.isNull(enrichedFormDetailsDTO)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, DATA_NOT_FOUND);
        }

        String formResponse = buildFormViewRes(enrichedFormDetailsDTO);
        return Response.ok(formResponse).build();
    }

    public void saveApiRequestLog(String utmSource, String appId, String pageType){
        ApiRequestLog apiRequestLog = new ApiRequestLog();
        apiRequestLog.setId(UUIDUtil.generateUUID());
        apiRequestLog.setAppId(appId);
        apiRequestLog.setPageType(pageType);
        apiRequestLog.setSource(utmSource);
        apiRequestLog.setDateCreated(new Date());
        apiRequestLog.setDateModified(new Date());
        dbMgmtFacade.saveRequestLog(apiRequestLog);
    }

    public void updateLatestFormInCache(){

        //TODO need to put threshold of 1 month
        List<ApplicationNameDetails> appNameList = dbMgmtFacade.fetchAllAppNames();
        if(CollectionUtils.isEmpty(appNameList)){
            return;
        }

        //TODO Below can done in async mode
        for(ApplicationNameDetails applicationNameDetails : appNameList){
            //fetch form detail from db
            EnrichedFormDetailsDTO enrichedFormDetailsDTO = getEnrichedFormDetails(
                    applicationNameDetails.getAppIdRef());

            if(Objects.isNull(enrichedFormDetailsDTO)){
                continue;
            }

            FormUtil.formCache.put(applicationNameDetails.getAppIdRef(),
                    buildFormViewRes(enrichedFormDetailsDTO));
        }
    }

    private String buildFormViewRes(EnrichedFormDetailsDTO enrichedFormDetailsDTO){

        ComponentRequestDTO componentRequestDTO = new ComponentRequestDTO();
        componentRequestDTO.setEnrichedFormDetailsDTO(enrichedFormDetailsDTO);

        FormViewResponseDTO formViewResponseDTO = new FormViewResponseDTO();

        populateApplicationResponse(formViewResponseDTO, componentRequestDTO);
        setRelatedFormList(enrichedFormDetailsDTO, formViewResponseDTO);

        return new Gson().toJson(formViewResponseDTO);
    }


    private void setRelatedFormList(EnrichedFormDetailsDTO enrichedFormDetailsDTO,
                                    FormViewResponseDTO formViewResponseDTO){

        ResponseManagement responseManagement = dbMgmtFacade.getResponseData(enrichedFormDetailsDTO.
                getApplicationFormDTO().getSectors());

        if(Objects.nonNull(responseManagement) && StringUtil.notEmpty(responseManagement.getRelatedForm())){
            RelatedFormResponseDTO relatedFormResponseDTO = new Gson().fromJson(responseManagement.getRelatedForm(),
                    RelatedFormResponseDTO.class);
            formViewResponseDTO.setRelatedFormResponseDTO(relatedFormResponseDTO);
        }

    }

    private void setTopAdmitCardList(AdmitCardResponseDTO admitCardResponseDTO){

        ResponseManagement responseManagement = dbMgmtFacade.getResponseData("ADMIT");

        if(Objects.nonNull(responseManagement) && StringUtil.notEmpty(responseManagement.getRelatedForm())){
            RelatedFormResponseDTO relatedFormResponseDTO = new Gson().fromJson(responseManagement.getRelatedForm(),
                    RelatedFormResponseDTO.class);
            admitCardResponseDTO.setRelatedFormResponseDTO(relatedFormResponseDTO);
        }
    }

    private void setTopResultList(ResultResponseDTO resultResponseDTO){

        ResponseManagement responseManagement = dbMgmtFacade.getResponseData("RESULT");

        if(Objects.nonNull(responseManagement) && StringUtil.notEmpty(responseManagement.getRelatedForm())){
            RelatedFormResponseDTO relatedFormResponseDTO = new Gson().fromJson(responseManagement.getRelatedForm(),
                    RelatedFormResponseDTO.class);
            resultResponseDTO.setRelatedFormResponseDTO(relatedFormResponseDTO);
        }
    }


    /**
     * Build ans key related form
     * @param ansKeyResponseDTO
     */
    private void setTopAnsKeyList(AnsKeyResponseDTO ansKeyResponseDTO){

        ResponseManagement responseManagement = dbMgmtFacade.getResponseData("ANSWER");

        if(Objects.nonNull(responseManagement) && StringUtil.notEmpty(responseManagement.getRelatedForm())){
            RelatedFormResponseDTO relatedFormResponseDTO = new Gson().fromJson(responseManagement.getRelatedForm(),
                    RelatedFormResponseDTO.class);
            ansKeyResponseDTO.setRelatedFormResponseDTO(relatedFormResponseDTO);
        }
    }

    public Response checkEligibility(EligibilityCheckRequestDTO eligibilityCheckRequestDTO){
        if(!FormValidator.isValidEligibilityRequest(eligibilityCheckRequestDTO)){
            return webUtils.invalidRequest();
        }

        ApplicationAgeDetails ageDetails = dbMgmtFacade.getApplicationAgeDetails(eligibilityCheckRequestDTO.getAppId());
        if(Objects.isNull(ageDetails)){
            return Response.ok(FormUtil.eligibilitySorryResponse()).build();
        }
        return verifyEligibility(ageDetails, eligibilityCheckRequestDTO);
    }

    public void populateApplicationResponse(FormViewResponseDTO formViewResponseDTO, ComponentRequestDTO componentRequestDTO){

        int i = 0;
        List<Future<?>> futureList = new ArrayList<>();
        for(ComponentEnum componentEnum: ComponentEnum.values()){
            Future<?> future = FormExecutorService.formResponseService.submit(()->
                    parseComponent(formViewResponseDTO, componentRequestDTO, i, componentEnum));
            futureList.add(future);
        }

        for(Future f : futureList){
            try {
                f.get();
            }catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
                log.warn("formResponseService Interrupted!", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public void parseComponent(FormViewResponseDTO formViewResponseDTO,
                               ComponentRequestDTO componentRequestDTO, int sortIndex,
                               ComponentEnum componentEnum){

        formViwerFactory.get(componentEnum)
                .parseResponse(componentRequestDTO, formViewResponseDTO, sortIndex);
    }

    public EnrichedFormDetailsDTO getEnrichedFormDetails(String appId) {

        ApplicationForm applicationForm = dbMgmtFacade.getApplicationForm(appId);

        EnrichedFormDetailsDTO enrichedFormDetailsDTO;
        if(Objects.nonNull(applicationForm)){
            log.info("Populating enriched form details appId::{}", appId);
            enrichedFormDetailsDTO = enrich(applicationForm, appId);
            if(Objects.nonNull(enrichedFormDetailsDTO)) {
                return enrichedFormDetailsDTO;
            }
        }
        return null;
    }


    public EnrichedFormDetailsDTO enrich(ApplicationForm applicationForm, String appId) {
        EnrichedFormDetailsDTO enrichedFormDetailsDTO = new EnrichedFormDetailsDTO();

        enrichedFormDetailsDTO.setApplicationFormDTO(objectMapper.convertValue(applicationForm, ApplicationFormDTO.class));

        ApplicationFeeDatails applicationFeeDatails = dbMgmtFacade.getApplicationFeeDetails(appId);
        enrichedFormDetailsDTO.setApplicationFeeDTO(objectMapper.convertValue(applicationFeeDatails, ApplicationFeeDTO.class));

        ApplicationAgeDetails applicationAgeDetails = dbMgmtFacade.getApplicationAgeDetails(appId);
        if(Objects.nonNull(applicationAgeDetails)) {
            enrichedFormDetailsDTO.setApplicationAgeDetailsDTO(objectMapper.convertValue(applicationAgeDetails, ApplicationAgeDetailsDTO.class));
        }

        ApplicationSeoDetails applicationSeoDetails = dbMgmtFacade.getApplicationSeoDetails(appId);
        if(Objects.nonNull(applicationSeoDetails)){
            enrichedFormDetailsDTO.setApplicationSeoDetailsDTO(objectMapper.convertValue(applicationSeoDetails, ApplicationSeoDetailsDTO.class));
        }

        List<ApplicationVacancyDetails> applicationVacancyDetails = dbMgmtFacade.getApplicationVacancyDetails(appId);
        List<ApplicationVacancyDTO> applicationVacancyDTOList = new ArrayList<>();
        for (ApplicationVacancyDetails vacancyDetails : applicationVacancyDetails) {
            applicationVacancyDTOList.add(objectMapper.convertValue(vacancyDetails, ApplicationVacancyDTO.class));
        }
        enrichedFormDetailsDTO.setApplicationVacancyDTOS(applicationVacancyDTOList);

        List<ApplicationContentManager> applicationContentManagersList = dbMgmtFacade.getApplicationContentDetails(appId);
        List<ApplicationContentManagerDTO> applicationContentManagerDTOList = new ArrayList<>();
        for(ApplicationContentManager applicationContentManager : applicationContentManagersList){
            applicationContentManagerDTOList.add(objectMapper.convertValue(applicationContentManager, ApplicationContentManagerDTO.class));
        }

        enrichedFormDetailsDTO.setApplicationContentManagerDTO(applicationContentManagerDTOList);

        ApplicationUrl applicationUrl = dbMgmtFacade.getApplicationUrl(appId);
        enrichedFormDetailsDTO.setApplicationUrlsDTO(objectMapper.convertValue(applicationUrl, ApplicationUrlsDTO.class));
        if(Objects.nonNull(applicationUrl) && Objects.nonNull(applicationUrl.getOthers())) {
            List<UrlManagerDTO> urlManagerDTOList = objectMapper.convertValue(applicationUrl.getOthers(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, UrlManagerDTO.class));

            enrichedFormDetailsDTO.getApplicationUrlsDTO().setUrlList(urlManagerDTOList);
        }


        List<ApplicationEligibilityDetails> applicationEligibilityDetails = dbMgmtFacade.fetchAllEligibility(appId);
        List<ApplicationEligibilityDTO> applicationEligibilityDTOList = new ArrayList<>();
        for(ApplicationEligibilityDetails eligibilityDetails : applicationEligibilityDetails) {
            applicationEligibilityDTOList.add(objectMapper.convertValue(eligibilityDetails, ApplicationEligibilityDTO.class));
        }
        enrichedFormDetailsDTO.setApplicationEligibilityDTOS(applicationEligibilityDTOList);

        return enrichedFormDetailsDTO;
    }

    private Response verifyEligibility(ApplicationAgeDetails ageDetails,
                                       EligibilityCheckRequestDTO eligibilityCheckRequestDTO){

        //max-dob- candidates dob should not be later than this date
        //min-dob- candidates dob should not be earlier than this (please consider age relaxation
        //In this case based on category
        if(Objects.isNull(ageDetails) ||
                Objects.isNull(ageDetails.getMaxNormalDob()) ||
                Objects.isNull(ageDetails.getMinNormalDob())){
            return Response.ok(FormUtil.eligibilitySorryResponse()).build();
        }

        if(eligibilityCheckRequestDTO.getDob().after(ageDetails.getMaxNormalDob())){
            return getEligibilityWithMaxDate(eligibilityCheckRequestDTO.getDob(), ageDetails.getMaxNormalDob());
        }
        if(eligibilityCheckRequestDTO.getDob().before(ageDetails.getMinNormalDob())){
            return getEligibilityWithMinDate(eligibilityCheckRequestDTO.getDob(), ageDetails.getMinNormalDob(), ageDetails,
                    eligibilityCheckRequestDTO.getCategory());
        }
        return Response.ok(FormUtil.eligibilitySuccessResponse()).build();
    }

    private Response getEligibilityWithMaxDate(Date dob, Date maxDate){
        long daysCount = DateUtils.compareDates(dob, maxDate);
        if(daysCount < 1){
            return Response.ok(FormUtil.eligibilitySuccessResponse()).build();
        }

        LocalDate startDate = LocalDate.of(1, 1, 1);
        LocalDate endDate = startDate.plusDays((int)daysCount - 1);
        // Create a period of given number of days
        Period period = Period.between(startDate, endDate);

        return Response.ok(FormUtil.eligibilityFailedByMaxAge(period.getYears(), period.getMonths(), period.getDays())).build();
    }

    private Response getEligibilityWithMinDate(Date dob, Date minDate, ApplicationAgeDetails ageDetails,
                                               String category){

        minDate = FormUtil.getAgeAfterRelaxed(minDate, ageDetails, category);
        long daysCount = DateUtils.compareDates(minDate, dob);

        if(daysCount < 1){
            return Response.ok(FormUtil.eligibilitySuccessResponse()).build();
        }

        // Create a period of given number of days
        LocalDate startDate = LocalDate.of(1, 1, 1);
        LocalDate endDate = startDate.plusDays((int)daysCount - 1);
        // Create a period of given number of days
        Period period = Period.between(startDate, endDate);

        return Response.ok(FormUtil.eligibilityFailedByMinAge(period.getYears(), period.getMonths(), period.getDays())).build();
    }


    public void buildAdmitCardResponse(AdmitCardResponseDTO admitCardResponseDTO,
                                       String admitId){

        GenericResponseV1 admitCard = dbMgmtFacade.fetchResponseV1ById(admitId);
        if(Objects.nonNull(admitCard)){
            admitCardResponseDTO.setAdmitCardIntroDTO(buildAdmitIntro(admitCard));
        }
        setTopAdmitCardList(admitCardResponseDTO);
        populateAdmitContent(admitCardResponseDTO, admitId);

        ApplicationSeoDetails applicationSeoDetails = dbMgmtFacade.getApplicationSeoDetails(admitId);
        if(Objects.nonNull(applicationSeoDetails)){
            admitCardResponseDTO.getAdmitCardIntroDTO().setSeoTitle(applicationSeoDetails.getTitle());
            admitCardResponseDTO.getAdmitCardIntroDTO().setSeoKeywords(applicationSeoDetails.getKeywords());
            admitCardResponseDTO.getAdmitCardIntroDTO().setSeoDescription(applicationSeoDetails.getDescription());
        }

    }


    public void buildResultResponse(ResultResponseDTO resultResponseDTO,
                                    String resultId){

        GenericResponseV1 resultResponse = dbMgmtFacade.fetchResponseV1ById(resultId);
        if(Objects.nonNull(resultResponse)){
            resultResponseDTO.setResultIntroDTO(buildResultIntro(resultResponse));
        }

        setTopResultList(resultResponseDTO);
        populateResultContent(resultResponseDTO, resultId);

        ApplicationSeoDetails applicationSeoDetails = dbMgmtFacade.getApplicationSeoDetails(resultId);
        if(Objects.nonNull(applicationSeoDetails)){
            resultResponseDTO.getResultIntroDTO().setSeoTitle(applicationSeoDetails.getTitle());
            resultResponseDTO.getResultIntroDTO().setSeoKeywords(applicationSeoDetails.getKeywords());
            resultResponseDTO.getResultIntroDTO().setSeoDescription(applicationSeoDetails.getDescription());
            resultResponseDTO.getResultIntroDTO().setShareLogoUrl(FormUtil.getPngLogoByName(resultResponse.getTitle()));
        }
    }


    /**
     * Build ans key third page response
     * @param ansKeyResponseDTO
     * @param ansKeyId
     */
    public void buildAnsKeyResponseP3(AnsKeyResponseDTO ansKeyResponseDTO,
                                      String ansKeyId){

        GenericResponseV1 ansKeyResponse = dbMgmtFacade.fetchResponseV1ById(ansKeyId);
        if(Objects.nonNull(ansKeyResponse)){
            ansKeyResponseDTO.setAnsKeyIntroDTO(buildAnsKeyIntro(ansKeyResponse));
        }

        setTopAnsKeyList(ansKeyResponseDTO);
        populateAnsKeyContent(ansKeyResponseDTO, ansKeyId);


        //Populate SEO part
        ApplicationSeoDetails applicationSeoDetails = dbMgmtFacade.getApplicationSeoDetails(ansKeyId);
        if(Objects.nonNull(applicationSeoDetails)){
            ansKeyResponseDTO.getAnsKeyIntroDTO().setSeoTitle(applicationSeoDetails.getTitle());
            ansKeyResponseDTO.getAnsKeyIntroDTO().setSeoKeywords(applicationSeoDetails.getKeywords());
            ansKeyResponseDTO.getAnsKeyIntroDTO().setSeoDescription(applicationSeoDetails.getDescription());
            ansKeyResponseDTO.getAnsKeyIntroDTO().setShareLogoUrl(FormUtil.getPngLogoByName(ansKeyResponse.getTitle()));
        }
    }

    public void buildAdmitCardResponseByAppId(AdmitCardResponseDTO admitCardResponseDTO, String appId){
        GenericResponseV1 admitCard = dbMgmtFacade.fetchResponseV1ByAppId(appId, "admit");

        if(Objects.nonNull(admitCard)){
            admitCardResponseDTO.setAdmitCardIntroDTO(buildAdmitIntro(admitCard));
        }

        populateAdmitContent(admitCardResponseDTO, admitCard.getId());
    }


    private void populateAdmitContent(AdmitCardResponseDTO admitCardResponseDTO, String admitId){
        GenericContentManager admitContentManager = dbMgmtFacade.fetchGenericContent(admitId);
        if(Objects.nonNull(admitContentManager)){
            AdmitContentManagerDTO admitContentManagerDTO = objectMapper.convertValue(admitContentManager, AdmitContentManagerDTO.class);
            admitCardResponseDTO.setAdmitContentManagerDTO(admitContentManagerDTO);
        }
    }

    private void populateResultContent(ResultResponseDTO resultResponseDTO, String resultId){
        GenericContentManager resultContentManager = dbMgmtFacade.fetchGenericContent(resultId);
        if(Objects.nonNull(resultContentManager)){
            ResultContentManagerDTO resultContentManagerDTO = objectMapper.convertValue(resultContentManager, ResultContentManagerDTO.class);
            resultResponseDTO.setResultContentManagerDTO(resultContentManagerDTO);
        }
    }


    /**
     * populate ans key content response
     * @param ansKeyResponseDTO
     * @param ansKeyId
     */
    private void populateAnsKeyContent(AnsKeyResponseDTO ansKeyResponseDTO, String ansKeyId){
        GenericContentManager ansKeyContentManager = dbMgmtFacade.fetchGenericContent(ansKeyId);
        if(Objects.nonNull(ansKeyContentManager)){
            AnsKeyContentDTO ansKeyContentDTO = objectMapper.convertValue(ansKeyContentManager, AnsKeyContentDTO.class);
            ansKeyResponseDTO.setAnsKeyContentDTO(ansKeyContentDTO);
        }
    }

    private AdmitCardIntroDTO buildAdmitIntro(GenericResponseV1 admitCard){
        AdmitCardIntroDTO admitCardIntroDTO = objectMapper.convertValue(admitCard, AdmitCardIntroDTO.class);

        admitCardIntroDTO.setAppIdRef(admitCard.getAppIdRef());
        admitCardIntroDTO.setAdmitCardName(admitCard.getTitle());
        admitCardIntroDTO.setReleaseDate(DateUtils.getFormatedDate1(admitCard.getDateCreated()));
        admitCardIntroDTO.setExamDateValue(admitCard.getShowDate());
        admitCardIntroDTO.setSubtitle("Government of India");

        long daysCount = DateUtils.getNoOfDaysFromToday(admitCard.getDateCreated());
        List<String> postedList = FormUtil.getPostedDetail(daysCount);

        admitCardIntroDTO.setPostedOn(postedList.get(0));
        admitCardIntroDTO.setPostedOnColor(postedList.get(1));

        admitCardIntroDTO.setLogoUrl(FormUtil.getLogoByName(admitCard.getTitle()));

        admitCardIntroDTO.setShareLogoUrl(FormUtil.getPngLogoByName(admitCard.getTitle()));

        return admitCardIntroDTO;
    }

    private ResultIntroDTO buildResultIntro(GenericResponseV1 resultDetails){
        ResultIntroDTO resultIntroDTO = objectMapper.convertValue(resultDetails, ResultIntroDTO.class);

        resultIntroDTO.setAppIdRef(resultDetails.getAppIdRef());
        resultIntroDTO.setDownloadUrl(resultDetails.getDownloadUrl());
        resultIntroDTO.setResultName(resultDetails.getTitle());
        resultIntroDTO.setResultDate(DateUtils.getFormatedDate1(resultDetails.getUpdatedDate()));
        resultIntroDTO.setSubtitle("Government of India");

        long daysCount = DateUtils.getNoOfDaysFromToday(resultDetails.getUpdatedDate());
        List<String> postedList = FormUtil.getPostedDetail(daysCount);

        resultIntroDTO.setPostedOn(postedList.get(0));
        resultIntroDTO.setPostedOnColor(postedList.get(1));

        resultIntroDTO.setLogoUrl(FormUtil.getLogoByName(resultDetails.getTitle()));

        return resultIntroDTO;
    }


    /**
     * Build AnsKeyIntro Details
     * @param ansKeyResponse
     * @return
     */
    private AnsKeyIntroDTO buildAnsKeyIntro(GenericResponseV1 ansKeyResponse){
        AnsKeyIntroDTO ansKeyIntroDTO = objectMapper.convertValue(ansKeyResponse, AnsKeyIntroDTO.class);

        ansKeyIntroDTO.setAppIdRef(ansKeyResponse.getAppIdRef());
        ansKeyIntroDTO.setAnsName(ansKeyResponse.getTitle());
        ansKeyIntroDTO.setDownloadUrl(ansKeyResponse.getDownloadUrl());
        ansKeyIntroDTO.setAnsDate(DateUtils.getFormatedDate1(ansKeyResponse.getUpdatedDate()));
        ansKeyIntroDTO.setSubtitle("Government of India");

        long daysCount = DateUtils.getNoOfDaysFromToday(ansKeyResponse.getUpdatedDate());
        List<String> postedList = FormUtil.getPostedDetail(daysCount);

        ansKeyIntroDTO.setPostedOn(postedList.get(0));
        ansKeyIntroDTO.setPostedOnColor(postedList.get(1));
        ansKeyIntroDTO.setLogoUrl(FormUtil.getLogoByName(ansKeyResponse.getTitle()));

        return ansKeyIntroDTO;
    }

}
