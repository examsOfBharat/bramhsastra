package com.examsofbharat.bramhsastra.akash.service;

import com.examsofbharat.bramhsastra.akash.utils.DateUtils;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.akash.utils.WebUtils;
import com.examsofbharat.bramhsastra.jal.constants.ErrorConstants;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
import com.examsofbharat.bramhsastra.jal.dto.response.FormLandingPageDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.LandingSectionDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.LandingSubSectionDTO;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.examsofbharat.bramhsastra.prithvi.entity.AdmitCard;
import com.examsofbharat.bramhsastra.prithvi.entity.ExamMetaData;
import com.examsofbharat.bramhsastra.prithvi.entity.ResponseManagement;
import com.examsofbharat.bramhsastra.prithvi.entity.ResultDetails;
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

@Service
@Slf4j
public class HomePageService {

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    @Autowired
    WebUtils webUtils;

    public Response buildLandingPageDto() {
        List<ExamMetaData> examMetaDataList = dbMgmtFacade.getExamMetaData();

        FormLandingPageDTO formLandingPageDTO = null;
        List<LandingSectionDTO> landingSectionDTOS = new ArrayList<>();
        //TODO shortIndex should be configurable (please try to configure from db)
        int sortIndex = 1;
        for (FormTypeEnum formType : FormTypeEnum.values()) {
            List<ExamMetaData> examList = examMetaDataList.stream()
                    .filter(examMetaData -> examMetaData.getExamCategory().equalsIgnoreCase(formType.name()))
                    .collect(Collectors.toList());

            landingSectionDTOS.add(buildAndParse(examList, formType, sortIndex));
            sortIndex++;

        }
        if(CollectionUtils.isEmpty(landingSectionDTOS)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.USER_NOT_FOUND);
        }
        formLandingPageDTO = new FormLandingPageDTO();
        formLandingPageDTO.setLandingSectionDTOS(landingSectionDTOS);

        String response = (new Gson()).toJson(formLandingPageDTO);
        pushResponseToDb("HOME_PAGE", response);

        return Response.ok().build();
    }

    public Response fetchHomeResponse(String responseType){
        ResponseManagement responseManagement = dbMgmtFacade.getResponseData(responseType);
        if(Objects.isNull(responseManagement)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.DATA_NOT_FOUND);
        }

        FormLandingPageDTO response = null;
        try {
            response = (new Gson()).fromJson(responseManagement.getResponse(), FormLandingPageDTO.class);
        }catch (Exception e){
             log.error("Exception occurred while parsing homeResponse responseType :: {}", responseType);
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.DATA_NOT_FOUND);
        }

        if(Objects.isNull(response)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.DATA_NOT_FOUND);
        }

        return Response.ok(response).build();
    }

    public LandingSectionDTO buildAndParse(List<ExamMetaData> examMetaDataList, FormTypeEnum formTypeEnum, int sortIndex) {

        boolean viewAll = (formTypeEnum.equals(FormTypeEnum.ADMIT)
                || formTypeEnum.equals(FormTypeEnum.RESULT))
                && examMetaDataList.size() > 6;

        LandingSectionDTO landingSectionDTO = buildSections(sortIndex, formTypeEnum, viewAll);
        List<LandingSubSectionDTO> landingSubSectionDTOList = new ArrayList<>();

        for (ExamMetaData examMetaData : examMetaDataList) {
            if (formTypeEnum.equals(FormTypeEnum.ADMIT)) {
                buildAdmitSubSections(landingSubSectionDTOList);
            } else if (formTypeEnum.equals(FormTypeEnum.RESULT)) {
                buildResultSubSection(landingSubSectionDTOList);
            } else {
                buildFormSubSections(landingSubSectionDTOList, examMetaData);
            }
        }
        landingSectionDTO.setSubSections(landingSubSectionDTOList);

        return landingSectionDTO;
    }

    public LandingSectionDTO buildSections(int sortIndex, FormTypeEnum formTypeEnum, boolean viewALl) {

        LandingSectionDTO landingSectionDTO = new LandingSectionDTO();
        landingSectionDTO.setType(formTypeEnum);
        landingSectionDTO.setTitle(formTypeEnum.getVal());
        landingSectionDTO.setSortIndex(sortIndex);
        landingSectionDTO.setViewAll(viewALl);

        return landingSectionDTO;
    }

    public void buildFormSubSections(List<LandingSubSectionDTO> landingSubSectionDTOS, ExamMetaData examMetaData) {

        LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();

        landingSubSectionDTO.setKey(FormSubTypeEnum.valueOf(examMetaData.getExamSubCategory()).name());
        landingSubSectionDTO.setTitle(FormSubTypeEnum.valueOf(examMetaData.getExamSubCategory()).getVal());
        landingSubSectionDTO.setShowDate(DateUtils.getFormatedDate1(examMetaData.getDateModified()));
        landingSubSectionDTO.setShowDateColor(FormUtil.getAdmitDateColor(examMetaData.getDateModified()));
        landingSubSectionDTO.setTotalApplication(examMetaData.getTotalForm());
        landingSubSectionDTO.setTotalVacancy(examMetaData.getTotalVacancy());

        landingSubSectionDTOS.add(landingSubSectionDTO);
    }

    public void buildAdmitSubSections(List<LandingSubSectionDTO> landingSubSectionDTOS) {

        //fetch admit card List
        List<AdmitCard> admitCardList = dbMgmtFacade.getAdmitCardList(5, "dateCreated");

        for (AdmitCard admitCard : admitCardList) {
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();

            landingSubSectionDTO.setKey("ADMIT");
            landingSubSectionDTO.setTitle(admitCard.getAdmitCardName());
            landingSubSectionDTO.setExamDate(DateUtils.getFormatedDate1(admitCard.getExamDate()));
            landingSubSectionDTO.setExamId(admitCard.getExamId());
            landingSubSectionDTO.setExamDateColor(FormUtil.getAdmitDateColor(admitCard.getExamDate()));

            landingSubSectionDTOS.add(landingSubSectionDTO);
        }
    }

    public void buildResultSubSection(List<LandingSubSectionDTO> landingSubSectionDTOS) {

        //fetch result list
        List<ResultDetails> resultDetailsList = dbMgmtFacade.getResultDetailList(5, "dateCreated");

        for (ResultDetails resultDetails : resultDetailsList) {
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();

            landingSubSectionDTO.setKey("RESULT");
            landingSubSectionDTO.setTitle(resultDetails.getResultName());
            landingSubSectionDTO.setShowDate(DateUtils.getFormatedDate1(resultDetails.getResultDate()));
            landingSubSectionDTO.setExamId(resultDetails.getExamId());
            landingSubSectionDTO.setShowDateColor(FormUtil.getAdmitDateColor(resultDetails.getResultDate()));

            landingSubSectionDTOS.add(landingSubSectionDTO);
        }

    }

    public void pushResponseToDb(String responseType, String response){
        ResponseManagement responseManagement = dbMgmtFacade.getResponseData(responseType);
        if(Objects.isNull(responseManagement)){
            responseManagement = new ResponseManagement();
            responseManagement.setDateCreated(new Date());
        }

        responseManagement.setResponseType(responseType);
        responseManagement.setResponse(response);
        responseManagement.setDateModified(new Date());

        dbMgmtFacade.saveResponseData(responseManagement);
    }
}
