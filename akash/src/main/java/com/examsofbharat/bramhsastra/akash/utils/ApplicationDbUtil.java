package com.examsofbharat.bramhsastra.akash.utils;

import com.examsofbharat.bramhsastra.jal.dto.AdmitCardDTO;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationFormDTO;
import com.examsofbharat.bramhsastra.jal.dto.ResultDetailsDTO;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.examsofbharat.bramhsastra.prithvi.entity.AdmitCard;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationForm;
import com.examsofbharat.bramhsastra.prithvi.entity.ResultDetails;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static final ObjectMapper mapper = new ObjectMapper();

    public String fetchResponseBasedOnSubType(FormSubTypeEnum formSubTypeEnum) {
        if(formSubTypeEnum.name().equals(FormSubTypeEnum.ADMIT.name())){
            List<AdmitCardDTO> admitCardList = getAdmitCardsList(0, 10);
            return (new Gson()).toJson(admitCardList);
        }

        if(formSubTypeEnum.name().equals(FormSubTypeEnum.RESULT.name())){
            List<ResultDetailsDTO> resultDetailsDTOList = getResultsList(0, 10);
            return  (new Gson()).toJson(resultDetailsDTOList);
        }

        List<ApplicationFormDTO> applicationFormDTOList = getApplicationsList(0,10, formSubTypeEnum);
        return  (new Gson()).toJson(applicationFormDTOList);
    }



    //fetch application form based on page, size, subtype and its formTypeEnum;
    public List<ApplicationFormDTO> getApplicationsList(int page, int size, FormSubTypeEnum subType) {
        FormTypeEnum formTypeEnum = FormUtil.getFormType(subType);
        List<ApplicationForm> applicationsList = dbMgmtFacade.fetchLatestApplicationsBasedOnType(page, size, subType.name(), formTypeEnum);
        if(CollectionUtils.isEmpty(applicationsList)) return null;

        List<ApplicationFormDTO> applicationsListDTO = new ArrayList<>();
        for(ApplicationForm application : applicationsList) {
            ApplicationFormDTO applicationFormDTO = mapper.convertValue(application, ApplicationFormDTO.class);
            applicationsListDTO.add(applicationFormDTO);
        }
        return applicationsListDTO;
    }

    //fetch admit card data based on page number and its record count
    public List<AdmitCardDTO> getAdmitCardsList(int page, int size) {
        List<AdmitCard> admitCardList = dbMgmtFacade.getLatestAdmitCardList(page, size, "dateCreated");
        List<AdmitCardDTO> admitCardDTOList = new ArrayList<>();
        for(AdmitCard admitCard : admitCardList) {
            AdmitCardDTO admitCardDTO = mapper.convertValue(admitCard, AdmitCardDTO.class);
            admitCardDTOList.add(admitCardDTO);
        }
        return admitCardDTOList;
    }


    //return result list based on page number and record count
    public List<ResultDetailsDTO> getResultsList(int page, int size) {
        List<ResultDetails> resultDetailsList = dbMgmtFacade.getResultDetailList(page, size, "dateCreated");

        List<ResultDetailsDTO> resultDetailsDTOList = new ArrayList<>();
        for(ResultDetails resultDetails : resultDetailsList) {
            ResultDetailsDTO resultDetailsDTO = mapper.convertValue(resultDetails, ResultDetailsDTO.class);
            resultDetailsDTOList.add(resultDetailsDTO);
        }
        return resultDetailsDTOList;
    }
}
