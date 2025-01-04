package com.examsofbharat.bramhsastra.akash.utils;

import com.examsofbharat.bramhsastra.jal.dto.RelatedFormDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.SecondaryPageDataDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.WrapperSecondaryPageDataDTO;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.entity.*;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.examsofbharat.bramhsastra.akash.constants.AkashConstants.*;

@Component
public class ApplicationDbUtil {

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    public String fetchSecDataAndRelatedData(String formSubTypeEnum, int page, int size, List<RelatedFormDTO> relatedForms) {
        WrapperSecondaryPageDataDTO wrapperSecondaryPageDataDTO = new WrapperSecondaryPageDataDTO();
        wrapperSecondaryPageDataDTO.setTitle(FormUtil.getSecondPageTitle(formSubTypeEnum));
        wrapperSecondaryPageDataDTO.setSeoDetailsDTO(FormUtil.getSecondPageSeo(formSubTypeEnum));

        if(formSubTypeEnum.equals(FormSubTypeEnum.ADMIT.name())){
            List<SecondaryPageDataDTO> admitCardList = getAdmitCardsList(page, size, formSubTypeEnum, relatedForms);
            wrapperSecondaryPageDataDTO.setFormList(admitCardList);
            return (new Gson()).toJson(wrapperSecondaryPageDataDTO);
        }

        if(formSubTypeEnum.equals(FormSubTypeEnum.RESULT.name())){
            List<SecondaryPageDataDTO> resultDetailsDTOList = getResultsList(page, size, formSubTypeEnum, relatedForms);
            wrapperSecondaryPageDataDTO.setFormList(resultDetailsDTOList);
            return  (new Gson()).toJson(wrapperSecondaryPageDataDTO);
        }

        if(formSubTypeEnum.equals(FormSubTypeEnum.ANS_KEY.name())){
            List<SecondaryPageDataDTO> ansKeyDataList = getGenericSecondPageList(page, size, formSubTypeEnum,relatedForms,"ANSKEY");
            wrapperSecondaryPageDataDTO.setFormList(ansKeyDataList);
            return (new Gson()).toJson(wrapperSecondaryPageDataDTO);
        }

        if(formSubTypeEnum.equals("UPCOMING")){
            return buildSecDataForUpcomingForm(page, size);
        }

        List<SecondaryPageDataDTO> applicationFormDTOList = getApplicationsList(page,size, formSubTypeEnum, relatedForms);
        wrapperSecondaryPageDataDTO.setFormList(applicationFormDTOList);
        return  (new Gson()).toJson(wrapperSecondaryPageDataDTO);
    }


    /**
     * 2nd page dto build for upcoming forms
     * fetch list from response and build here (name and date)
     * @param page
     * @param size
     * @return
     */
    public String buildSecDataForUpcomingForm(int page, int size){


        List<UpcomingForms> upcomingFormsList = dbMgmtFacade.fetchLatestUpcomingForm(page,size,"dateCreated");
        List<SecondaryPageDataDTO> upcomingSecData = new ArrayList<>();

        int color = 0;
        for(UpcomingForms upcomingForms : upcomingFormsList){
            SecondaryPageDataDTO secondaryPageDataDTO = new SecondaryPageDataDTO();
            secondaryPageDataDTO.setTitle(upcomingForms.getAppName());
            secondaryPageDataDTO.setReleaseDate(upcomingForms.getExpDate());

            secondaryPageDataDTO.setQualificationList(Arrays.stream(
                    upcomingForms.getQualification().split(",")).toList());

            secondaryPageDataDTO.setExpVacancy(upcomingForms.getExpVacancy());
            secondaryPageDataDTO.setLogoUrl(FormUtil.getLogoByName(upcomingForms.getAppName()));
            secondaryPageDataDTO.setCardColor(FormUtil.fetchSecCardColor(color%4));
            color++;

            upcomingSecData.add(secondaryPageDataDTO);
        }

        WrapperSecondaryPageDataDTO wrapperSecondaryPageDataDTO = new WrapperSecondaryPageDataDTO();
        wrapperSecondaryPageDataDTO.setTitle("Upcoming Forms");
        wrapperSecondaryPageDataDTO.setSeoDetailsDTO(FormUtil.getSecondPageSeo("UPCOMING"));

        wrapperSecondaryPageDataDTO.setFormList(upcomingSecData);

        return  (new Gson()).toJson(wrapperSecondaryPageDataDTO);
    }





    //fetch application form based on page, size, subtype and its formTypeEnum;
    public List<SecondaryPageDataDTO> getApplicationsList(int page, int size, String subType, List<RelatedFormDTO> relatedForms) {

        List<ApplicationForm> applicationsList;

        //Actually these are not subType but have handled to reUse code
        if(subType.equals(FormTypeEnum.LATEST_FORMS.name())){
            applicationsList = dbMgmtFacade.fetchAllLatestApp(page, size);
        } else if (subType.equals(FormTypeEnum.OLDER_FORMS.name())) {
            applicationsList = dbMgmtFacade.fetchAllOldestApp(page, size);
        }else if(subType.equals(FormTypeEnum.POPULAR_FORMS.name())){
            applicationsList = filterPopularForms(dbMgmtFacade.fetchAllLatestApp(page, size));
        }else {
            FormTypeEnum formTypeEnum = FormUtil.getFormType(subType);
            applicationsList = dbMgmtFacade.fetchLatestApplicationsBasedOnType(page, size, subType, formTypeEnum);
        }

        if(CollectionUtils.isEmpty(applicationsList)) return null;

        List<SecondaryPageDataDTO> applicationsListDTO = new ArrayList<>();
        int color = 0;
        int color2 = 0;
        for(ApplicationForm application : applicationsList) {
            //at the time of 0th page we will populate related form and keep in response-management
            if(relatedForms != null && FormUtil.getSectorFormTypeList().contains(subType)){
                relatedForms.add(buildRelatedForm(application, color));
                color++;
            }
            applicationsListDTO.add(buildFormSecondaryPage(application, subType, color2));
            color2++;
        }
        return applicationsListDTO;
    }

    public List<ApplicationForm> filterPopularForms(List<ApplicationForm> applicationsList){

        List<ApplicationForm> popularFormsList = new ArrayList<>();

        for(ApplicationForm application : applicationsList){
            if(application.getTotalVacancy() > 0){
                popularFormsList.add(application);
            }
        }
        return popularFormsList;
    }


    //fetch admit card data based on page number and its record count
    public List<SecondaryPageDataDTO> getAdmitCardsList(int page, int size, String subType, List<RelatedFormDTO> relatedForms) {
        List<GenericResponseV1> admitCardList = dbMgmtFacade.getLatestResponseV1List(page, size, "dateCreated","ADMIT");
        List<SecondaryPageDataDTO> admitCardDTOList = new ArrayList<>();
        int color = 0;
        for(GenericResponseV1 admitCard : admitCardList) {
            //at the time of 0th page we will populate related form and keep in response-management
            if(relatedForms != null){
                relatedForms.add(buildRelatedAdmit(admitCard, color));
                color++;
            }
            admitCardDTOList.add(buildAdmitSecondaryPage(admitCard, subType, color));
        }
        return admitCardDTOList;
    }


    //return result list based on page number and record count
    public List<SecondaryPageDataDTO> getResultsList(int page, int size, String subType,List<RelatedFormDTO> relatedForms) {
        List<GenericResponseV1> resultDetailsList = dbMgmtFacade.getLatestResponseV1List(page, size, "dateCreated","RESULT");

        List<SecondaryPageDataDTO> resultDetailsDTOList = new ArrayList<>();
        int color = 0;
        int color1 = 0;
        for(GenericResponseV1 resultDetails : resultDetailsList) {
            if(relatedForms != null){
                relatedForms.add(buildRelatedResult(resultDetails, color));
                color++;
            }
            resultDetailsDTOList.add(buildResultSecondaryPage(resultDetails, subType, color1));
            color1++;
        }
        return resultDetailsDTOList;
    }

    //return result list based on page number and record count
    public List<SecondaryPageDataDTO> getAnsKeyList(int page, int size, String subType) {

        String dateType = "answerDate";
        Date startDate = DateUtils.addDays(new Date(), -5);
        List<ApplicationForm> ansKeyList = dbMgmtFacade.getFormWithAnsKey(page, size, dateType, startDate);

        List<SecondaryPageDataDTO> resultDetailsDTOList = new ArrayList<>();
        int color = 0;
        for(ApplicationForm applicationForm : ansKeyList) {
            resultDetailsDTOList.add(buildAnsKeySecondaryPage(applicationForm, subType, color));
        }
        return resultDetailsDTOList;
    }


    /**
     * Build second page for admit card, result and ansKey
     * It will be generic function for all three pageTypes
     * @param page @mandatory
     * @param size @mandatory
     * @param subType @mandatory
     * @param relatedForms @mandatory
     * @return
     */
    public List<SecondaryPageDataDTO> getGenericSecondPageList(int page, int size, String subType,List<RelatedFormDTO> relatedForms, String type) {
        List<GenericResponseV1> genericResponseList = dbMgmtFacade.getLatestResponseV1List(page, size, "dateCreated",type);

        List<SecondaryPageDataDTO> secondPageDetailsList = new ArrayList<>();
        int color = 0;
        int color1 = 0;
        for(GenericResponseV1 genericResponse : genericResponseList) {
            if (relatedForms != null) {
                relatedForms.add(buildRelatedResult(genericResponse, color));
                color++;
            }
            secondPageDetailsList.add(buildAnsKeySecondPage(genericResponse, subType, color1));
            color1++;
        }
        return secondPageDetailsList;
    }


    public SecondaryPageDataDTO buildAdmitSecondaryPage(GenericResponseV1 admitCard, String subType, int i){
        SecondaryPageDataDTO secondaryPageDataDTO = new SecondaryPageDataDTO();

        secondaryPageDataDTO.setId(admitCard.getId());
        secondaryPageDataDTO.setPageType("admit");
        secondaryPageDataDTO.setTitle(admitCard.getTitle());
        secondaryPageDataDTO.setExamDate(admitCard.getShowDate());
        secondaryPageDataDTO.setExamDateColor(GREEN_COLOR);
        secondaryPageDataDTO.setReleaseDate(DateUtils.getFormatedDate1(admitCard.getDateCreated()));
        secondaryPageDataDTO.setReleaseDateColor(GREEN_COLOR);
        secondaryPageDataDTO.setNewFlag(FormUtil.dateIsWithinXDays(admitCard.getDateCreated()));
        secondaryPageDataDTO.setSubType(subType);
        if(FormUtil.dateIsWithin2Days(admitCard.getDateCreated())){
            secondaryPageDataDTO.setFormStatus("NEW");
        }
        secondaryPageDataDTO.setCardColor(FormUtil.fetchCardColor(i%4));

        //TODO try to get sort title from admin if possible
        //convert title into url title
        secondaryPageDataDTO.setUrlTitle(FormUtil.getUrlTitle(admitCard.getTitle()));

        return secondaryPageDataDTO;
    }

    public SecondaryPageDataDTO buildResultSecondaryPage(GenericResponseV1 resultDetails, String subType, int color){

        SecondaryPageDataDTO secondaryPageDataDTO = new SecondaryPageDataDTO();

        secondaryPageDataDTO.setId(resultDetails.getId());
        secondaryPageDataDTO.setPageType("result");
        secondaryPageDataDTO.setTitle(resultDetails.getTitle());
        secondaryPageDataDTO.setCardColor(FormUtil.fetchCardColor(color%4));

        secondaryPageDataDTO.setReleaseDate(DateUtils.getFormatedDate1(resultDetails.getUpdatedDate()));
        secondaryPageDataDTO.setReleaseDateColor(FormUtil.getLastXDaysDateColor(resultDetails.getUpdatedDate()));
        secondaryPageDataDTO.setNewFlag(FormUtil.dateIsWithinXDays(resultDetails.getUpdatedDate()));
        secondaryPageDataDTO.setSubType(subType);
        if(FormUtil.dateIsWithin2Days(resultDetails.getDateCreated())){
            secondaryPageDataDTO.setFormStatus("NEW");
        }

        //convert title into url title
        secondaryPageDataDTO.setUrlTitle(FormUtil.getUrlTitle(resultDetails.getTitle()));

        return secondaryPageDataDTO;
    }

    /**
     * Build ans key second page
     * @param anskeyDetails
     * @param subType
     * @param color
     * @return
     */
    public SecondaryPageDataDTO buildAnsKeySecondPage(GenericResponseV1 anskeyDetails, String subType, int color){

        SecondaryPageDataDTO secondaryPageDataDTO = new SecondaryPageDataDTO();

        secondaryPageDataDTO.setId(anskeyDetails.getId());
        secondaryPageDataDTO.setPageType("anskey");
        secondaryPageDataDTO.setTitle(anskeyDetails.getTitle());
        secondaryPageDataDTO.setCardColor(FormUtil.fetchCardColor(color%4));

        secondaryPageDataDTO.setReleaseDate(DateUtils.getFormatedDate1(anskeyDetails.getUpdatedDate()));
        secondaryPageDataDTO.setReleaseDateColor(FormUtil.getLastXDaysDateColor(anskeyDetails.getUpdatedDate()));
        secondaryPageDataDTO.setNewFlag(FormUtil.dateIsWithinXDays(anskeyDetails.getUpdatedDate()));
        secondaryPageDataDTO.setSubType(subType);
        if(FormUtil.dateIsWithin2Days(anskeyDetails.getDateCreated())){
            secondaryPageDataDTO.setFormStatus("NEW");
        }

        //convert title into url title
        secondaryPageDataDTO.setUrlTitle(FormUtil.getUrlTitle(anskeyDetails.getTitle()));

        return secondaryPageDataDTO;
    }

    public SecondaryPageDataDTO buildAnsKeySecondaryPage(ApplicationForm applicationForm, String subType, int i){

        SecondaryPageDataDTO secondaryPageDataDTO = new SecondaryPageDataDTO();

        secondaryPageDataDTO.setPageType("answer");
        secondaryPageDataDTO.setCardColor(FormUtil.fetchCardColor(i%4));
        secondaryPageDataDTO.setExtraField(applicationForm.getAnswerKeyUrl());
        secondaryPageDataDTO.setTitle(applicationForm.getExamName());
        secondaryPageDataDTO.setReleaseDate(DateUtils.getFormatedDate1(applicationForm.getAnswerDate()));
        secondaryPageDataDTO.setReleaseDateColor(FormUtil.getLastXDaysDateColor(applicationForm.getAnswerDate()));
        secondaryPageDataDTO.setSubType(subType);

        return secondaryPageDataDTO;
    }

    public SecondaryPageDataDTO buildFormSecondaryPage(ApplicationForm applicationForm, String subType, int i){

        SecondaryPageDataDTO secondaryPageDataDTO = new SecondaryPageDataDTO();

        secondaryPageDataDTO.setId(applicationForm.getId());
        secondaryPageDataDTO.setPageType("form");
        secondaryPageDataDTO.setTitle(applicationForm.getExamName());

        secondaryPageDataDTO.setReleaseDate(DateUtils.getFormatedDate1(applicationForm.getDateCreated()));
//        secondaryPageDataDTO.setReleaseDateColor(FormUtil.getLastXDaysDateColor(applicationForm.getStartDate()));

        if(applicationForm.getTotalVacancy() > 0) {
            secondaryPageDataDTO.setTotalVacancy(FormUtil.formatIntoIndianNumSystem(applicationForm.getTotalVacancy()));
        }else{
            secondaryPageDataDTO.setTotalVacancy("Not Available");
        }

        if(StringUtil.notEmpty(applicationForm.getResultId())){
            secondaryPageDataDTO.setStatus("Result out");
        } else if (StringUtil.notEmpty(applicationForm.getAnswerKeyUrl())) {
            secondaryPageDataDTO.setStatus("Answer key out");
        } else if(StringUtil.notEmpty(applicationForm.getAdmitId())){
            secondaryPageDataDTO.setStatus("Admit card released");
        }

        secondaryPageDataDTO.setSubType(subType);
        secondaryPageDataDTO.setCardColor(FormUtil.fetchCardColor(i%4));
        secondaryPageDataDTO.setLastDate(DateUtils.getFormatedDate1(applicationForm.getEndDate()));
        secondaryPageDataDTO.setLastDateColor(FormUtil.getExpiryDateColor(applicationForm.getEndDate()));
        secondaryPageDataDTO.setNewFlag(FormUtil.dateIsWithinXDays(applicationForm.getStartDate()));

        //form status banner condition
        if(applicationForm.getDateModified().compareTo(DateUtils.addDays(applicationForm.getDateCreated(), 5)) > 0){
            secondaryPageDataDTO.setFormStatus("UPDATES");
            secondaryPageDataDTO.setReleaseDateColor(BLUE_COLOR_CODE);
        }else if(new Date().after(applicationForm.getEndDate())){
            secondaryPageDataDTO.setFormStatus("EXPIRED");
            secondaryPageDataDTO.setReleaseDateColor(RED_COLOR);
        }else if(FormUtil.dateIsWithinXDays(applicationForm.getStartDate())){
            secondaryPageDataDTO.setFormStatus("NEW");
            secondaryPageDataDTO.setReleaseDateColor(GREEN_COLOR);
        }else{
            secondaryPageDataDTO.setReleaseDateColor(BLUE_COLOR_CODE);
        }

        //convert title into url title
        secondaryPageDataDTO.setUrlTitle(FormUtil.getUrlTitle(FormUtil.getUrlTitle(applicationForm)));

        return secondaryPageDataDTO;
    }

    private RelatedFormDTO buildRelatedForm(ApplicationForm applicationForm, int color){
        RelatedFormDTO relatedFormDTO = new RelatedFormDTO();

        relatedFormDTO.setId(applicationForm.getId());
        relatedFormDTO.setName(applicationForm.getExamName());
        relatedFormDTO.setPageType("form");
        relatedFormDTO.setReleaseDate(DateUtils.getFormatedDate1(applicationForm.getStartDate()));
        relatedFormDTO.setCardColor(FormUtil.fetchSecCardColor(color%4));

        return relatedFormDTO;
    }

    private RelatedFormDTO buildRelatedAdmit(GenericResponseV1 admitCard, int color){
        RelatedFormDTO relatedFormDTO = new RelatedFormDTO();

        relatedFormDTO.setId(admitCard.getId());
        relatedFormDTO.setPageType("admit");
        relatedFormDTO.setName(admitCard.getTitle());
        relatedFormDTO.setReleaseDate(DateUtils.getFormatedDate1(admitCard.getDateCreated()));
        relatedFormDTO.setCardColor(FormUtil.fetchSecCardColor(color%4));

        return relatedFormDTO;
    }

    private RelatedFormDTO buildRelatedResult(GenericResponseV1 resultDetails, int color){
        RelatedFormDTO relatedFormDTO = new RelatedFormDTO();

        relatedFormDTO.setId(resultDetails.getId());
        relatedFormDTO.setPageType("result");
        relatedFormDTO.setName(resultDetails.getTitle());
        relatedFormDTO.setReleaseDate(DateUtils.getFormatedDate1(resultDetails.getUpdatedDate()));
        relatedFormDTO.setCardColor(FormUtil.fetchSecCardColor(color%4));

        return relatedFormDTO;
    }


}
