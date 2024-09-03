package com.examsofbharat.bramhsastra.akash.utils;

import com.examsofbharat.bramhsastra.jal.dto.RelatedFormDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.SecondaryPageDataDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.WrapperSecondaryPageDataDTO;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.entity.AdmitCard;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationForm;
import com.examsofbharat.bramhsastra.prithvi.entity.ResultDetails;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
            List<SecondaryPageDataDTO> ansKeyDataList = getAnsKeyList(page, size, formSubTypeEnum);
            wrapperSecondaryPageDataDTO.setFormList(ansKeyDataList);
            return (new Gson()).toJson(wrapperSecondaryPageDataDTO);
        }

        List<SecondaryPageDataDTO> applicationFormDTOList = getApplicationsList(page,size, formSubTypeEnum, relatedForms);
        wrapperSecondaryPageDataDTO.setFormList(applicationFormDTOList);
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


    //fetch admit card data based on page number and its record count
    public List<SecondaryPageDataDTO> getAdmitCardsList(int page, int size, String subType, List<RelatedFormDTO> relatedForms) {
        List<AdmitCard> admitCardList = dbMgmtFacade.getLatestAdmitCardList(page, size, "dateCreated");
        List<SecondaryPageDataDTO> admitCardDTOList = new ArrayList<>();
        int color = 0;
        for(AdmitCard admitCard : admitCardList) {
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
        List<ResultDetails> resultDetailsList = dbMgmtFacade.getResultDetailList(page, size, "dateCreated");

        List<SecondaryPageDataDTO> resultDetailsDTOList = new ArrayList<>();
        int color = 0;
        int color1 = 0;
        for(ResultDetails resultDetails : resultDetailsList) {
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


    public SecondaryPageDataDTO buildAdmitSecondaryPage(AdmitCard admitCard, String subType, int i){
        SecondaryPageDataDTO secondaryPageDataDTO = new SecondaryPageDataDTO();

        secondaryPageDataDTO.setId(admitCard.getId());
        secondaryPageDataDTO.setPageType("admit");
        secondaryPageDataDTO.setTitle(admitCard.getAdmitCardName());
        secondaryPageDataDTO.setExamDate(DateUtils.getFormatedDate1(admitCard.getExamDate()));
        secondaryPageDataDTO.setExamDateColor(FormUtil.getLastXDaysDateColor(admitCard.getExamDate()));
        secondaryPageDataDTO.setReleaseDate(DateUtils.getFormatedDate1(admitCard.getDateCreated()));
        secondaryPageDataDTO.setReleaseDateColor(FormUtil.getLastXDaysDateColor(admitCard.getDateCreated()));
        secondaryPageDataDTO.setNewFlag(FormUtil.dateIsWithinXDays(admitCard.getDateCreated()));
        secondaryPageDataDTO.setSubType(subType);
        if(FormUtil.dateIsWithin2Days(admitCard.getDateCreated())){
            secondaryPageDataDTO.setFormStatus("NEW");
        }
        secondaryPageDataDTO.setCardColor(FormUtil.fetchCardColor(i%4));

        return secondaryPageDataDTO;
    }

    public SecondaryPageDataDTO buildResultSecondaryPage(ResultDetails resultDetails, String subType, int color){

        SecondaryPageDataDTO secondaryPageDataDTO = new SecondaryPageDataDTO();

        secondaryPageDataDTO.setId(resultDetails.getId());
        secondaryPageDataDTO.setPageType("result");
        secondaryPageDataDTO.setTitle(resultDetails.getResultName());
        secondaryPageDataDTO.setCardColor(FormUtil.fetchCardColor(color%4));

        secondaryPageDataDTO.setReleaseDate(DateUtils.getFormatedDate1(resultDetails.getResultDate()));
        secondaryPageDataDTO.setReleaseDateColor(FormUtil.getLastXDaysDateColor(resultDetails.getResultDate()));
        secondaryPageDataDTO.setNewFlag(FormUtil.dateIsWithinXDays(resultDetails.getResultDate()));
        secondaryPageDataDTO.setSubType(subType);
        if(FormUtil.dateIsWithin2Days(resultDetails.getDateCreated())){
            secondaryPageDataDTO.setFormStatus("NEW");
        }

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

        secondaryPageDataDTO.setTotalVacancy(applicationForm.getTotalVacancy());

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

        if(applicationForm.getDateModified().compareTo(DateUtils.addDays(applicationForm.getDateCreated(), 5)) > 0){
            secondaryPageDataDTO.setFormStatus("UPDATES");
            secondaryPageDataDTO.setReleaseDateColor(RED_COLOR);
        }else if(new Date().after(applicationForm.getEndDate())){
            secondaryPageDataDTO.setFormStatus("EXPIRED");
            secondaryPageDataDTO.setReleaseDateColor(RED_COLOR);
        }else if (FormUtil.dateIsWithinXDays(applicationForm.getStartDate())){
            secondaryPageDataDTO.setFormStatus("NEW");
            secondaryPageDataDTO.setReleaseDateColor(GREEN_COLOR);
        }else{
            secondaryPageDataDTO.setReleaseDateColor(BLUE_COLOR_CODE);
        }

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

    private RelatedFormDTO buildRelatedAdmit(AdmitCard admitCard, int color){
        RelatedFormDTO relatedFormDTO = new RelatedFormDTO();

        relatedFormDTO.setId(admitCard.getId());
        relatedFormDTO.setPageType("admit");
        relatedFormDTO.setName(admitCard.getAdmitCardName());
        relatedFormDTO.setReleaseDate(DateUtils.getFormatedDate1(admitCard.getDateCreated()));
        relatedFormDTO.setCardColor(FormUtil.fetchSecCardColor(color%4));

        return relatedFormDTO;
    }

    private RelatedFormDTO buildRelatedResult(ResultDetails resultDetails, int color){
        RelatedFormDTO relatedFormDTO = new RelatedFormDTO();

        relatedFormDTO.setId(resultDetails.getId());
        relatedFormDTO.setPageType("result");
        relatedFormDTO.setName(resultDetails.getResultName());
        relatedFormDTO.setReleaseDate(DateUtils.getFormatedDate1(resultDetails.getResultDate()));
        relatedFormDTO.setCardColor(FormUtil.fetchSecCardColor(color%4));

        return relatedFormDTO;
    }


}
