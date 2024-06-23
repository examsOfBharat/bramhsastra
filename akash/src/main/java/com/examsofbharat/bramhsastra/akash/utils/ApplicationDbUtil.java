package com.examsofbharat.bramhsastra.akash.utils;

import com.examsofbharat.bramhsastra.jal.dto.RelatedFormDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.SecondaryPageDataDTO;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.examsofbharat.bramhsastra.prithvi.entity.AdmitCard;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationForm;
import com.examsofbharat.bramhsastra.prithvi.entity.ResultDetails;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationDbUtil {

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    public String fetchResponseBasedOnSubType(String formSubTypeEnum, int page, int size, List<RelatedFormDTO> relatedForms) {
        if(formSubTypeEnum.equals(FormSubTypeEnum.ADMIT.name())){
            List<SecondaryPageDataDTO> admitCardList = getAdmitCardsList(page, size, formSubTypeEnum, relatedForms);
            return (new Gson()).toJson(admitCardList);
        }

        if(formSubTypeEnum.equals(FormSubTypeEnum.RESULT.name())){
            List<SecondaryPageDataDTO> resultDetailsDTOList = getResultsList(page, size, formSubTypeEnum, relatedForms);
            return  (new Gson()).toJson(resultDetailsDTOList);
        }

        List<SecondaryPageDataDTO> applicationFormDTOList = getApplicationsList(page,size, formSubTypeEnum, relatedForms);
        return  (new Gson()).toJson(applicationFormDTOList);
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
            FormTypeEnum formTypeEnum = FormUtil.getFormType(FormSubTypeEnum.valueOf(subType));
            applicationsList = dbMgmtFacade.fetchLatestApplicationsBasedOnType(page, size, subType, formTypeEnum);
        }

        if(CollectionUtils.isEmpty(applicationsList)) return null;

        List<SecondaryPageDataDTO> applicationsListDTO = new ArrayList<>();
        int color = 0;
        for(ApplicationForm application : applicationsList) {
            //at the time of 0th page we will populate related form and keep in response-management
            if(relatedForms != null && FormUtil.getRelatedFormType().contains(subType)){
                relatedForms.add(buildRelatedForm(application, color));
                color++;
            }
            applicationsListDTO.add(buildFormSecondaryPage(application, subType));
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
            admitCardDTOList.add(buildAdmitSecondaryPage(admitCard, subType));
        }
        return admitCardDTOList;
    }


    //return result list based on page number and record count
    public List<SecondaryPageDataDTO> getResultsList(int page, int size, String subType,List<RelatedFormDTO> relatedForms) {
        List<ResultDetails> resultDetailsList = dbMgmtFacade.getResultDetailList(page, size, "dateCreated");

        List<SecondaryPageDataDTO> resultDetailsDTOList = new ArrayList<>();
        int color = 0;
        for(ResultDetails resultDetails : resultDetailsList) {
            if(relatedForms != null){
                relatedForms.add(buildRelatedResult(resultDetails, color));
                color++;
            }
            resultDetailsDTOList.add(buildResultSecondaryPage(resultDetails, subType));
        }
        return resultDetailsDTOList;
    }


    public SecondaryPageDataDTO buildAdmitSecondaryPage(AdmitCard admitCard, String subType){
        SecondaryPageDataDTO secondaryPageDataDTO = new SecondaryPageDataDTO();

        secondaryPageDataDTO.setId(admitCard.getId());
        secondaryPageDataDTO.setPageType("admit");
        secondaryPageDataDTO.setTitle(admitCard.getAdmitCardName());
        secondaryPageDataDTO.setExamDate(DateUtils.getFormatedDate1(admitCard.getExamDate()));
        secondaryPageDataDTO.setExamDateColor(FormUtil.getLastXDaysDateColor(admitCard.getExamDate()));

        secondaryPageDataDTO.setReleaseDate(DateUtils.getFormatedDate1(admitCard.getDateCreated()));
        secondaryPageDataDTO.setReleaseDateColor(FormUtil.getLastXDaysDateColor(admitCard.getDateCreated()));
        secondaryPageDataDTO.setSubType(subType);

        return secondaryPageDataDTO;
    }

    public SecondaryPageDataDTO buildResultSecondaryPage(ResultDetails resultDetails, String subType){

        SecondaryPageDataDTO secondaryPageDataDTO = new SecondaryPageDataDTO();

        secondaryPageDataDTO.setId(resultDetails.getId());
        secondaryPageDataDTO.setPageType("result");
        secondaryPageDataDTO.setTitle(resultDetails.getResultName());

        secondaryPageDataDTO.setReleaseDate(DateUtils.getFormatedDate1(resultDetails.getDateCreated()));
        secondaryPageDataDTO.setReleaseDateColor(FormUtil.getLastXDaysDateColor(resultDetails.getDateCreated()));
        secondaryPageDataDTO.setSubType(subType);

        return secondaryPageDataDTO;
    }

    public SecondaryPageDataDTO buildFormSecondaryPage(ApplicationForm applicationForm, String subType){

        SecondaryPageDataDTO secondaryPageDataDTO = new SecondaryPageDataDTO();

        secondaryPageDataDTO.setId(applicationForm.getId());
        secondaryPageDataDTO.setPageType("form");
        secondaryPageDataDTO.setTitle(applicationForm.getExamName());

        secondaryPageDataDTO.setReleaseDate(DateUtils.getFormatedDate1(applicationForm.getDateCreated()));
        secondaryPageDataDTO.setReleaseDateColor(FormUtil.getLastXDaysDateColor(applicationForm.getDateCreated()));

        secondaryPageDataDTO.setTotalVacancy(applicationForm.getTotalVacancy());

        secondaryPageDataDTO.setSubType(subType);

        return secondaryPageDataDTO;
    }

    private RelatedFormDTO buildRelatedForm(ApplicationForm applicationForm, int color){
        RelatedFormDTO relatedFormDTO = new RelatedFormDTO();

        relatedFormDTO.setId(applicationForm.getId());
        relatedFormDTO.setName(applicationForm.getExamName());
        relatedFormDTO.setPageType("form");
        relatedFormDTO.setReleaseDate(DateUtils.getFormatedDate1(applicationForm.getDateCreated()));
        relatedFormDTO.setCardColor(FormUtil.fetchCardColor(color%4));

        return relatedFormDTO;
    }

    private RelatedFormDTO buildRelatedAdmit(AdmitCard admitCard, int color){
        RelatedFormDTO relatedFormDTO = new RelatedFormDTO();

        relatedFormDTO.setId(admitCard.getId());
        relatedFormDTO.setPageType("admit");
        relatedFormDTO.setName(admitCard.getAdmitCardName());
        relatedFormDTO.setReleaseDate(DateUtils.getFormatedDate1(admitCard.getDateCreated()));
        relatedFormDTO.setCardColor(FormUtil.fetchCardColor(color%4));

        return relatedFormDTO;
    }

    private RelatedFormDTO buildRelatedResult(ResultDetails resultDetails, int color){
        RelatedFormDTO relatedFormDTO = new RelatedFormDTO();

        relatedFormDTO.setId(resultDetails.getId());
        relatedFormDTO.setPageType("result");
        relatedFormDTO.setName(resultDetails.getResultName());
        relatedFormDTO.setReleaseDate(DateUtils.getFormatedDate1(resultDetails.getDateCreated()));
        relatedFormDTO.setCardColor(FormUtil.fetchCardColor(color%4));

        return relatedFormDTO;
    }


}
