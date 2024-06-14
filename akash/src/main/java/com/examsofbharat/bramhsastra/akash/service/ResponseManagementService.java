package com.examsofbharat.bramhsastra.akash.service;

import com.examsofbharat.bramhsastra.akash.constants.AkashConstants;
import com.examsofbharat.bramhsastra.akash.executor.FormExecutorService;
import com.examsofbharat.bramhsastra.akash.utils.ApplicationDbUtil;
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

@Service
@Slf4j
public class ResponseManagementService {

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    @Autowired
    WebUtils webUtils;

    @Autowired
    ApplicationDbUtil applicationDbUtil;

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

        buildAndSaveResponseToDb();

        return webUtils.buildSuccessResponse("SUCCESS");
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
                || formTypeEnum.equals(FormTypeEnum.RESULT));

        LandingSectionDTO landingSectionDTO = buildSections(sortIndex, formTypeEnum, viewAll);
        List<LandingSubSectionDTO> landingSubSectionDTOList = new ArrayList<>();

        int i = 0;
        for (ExamMetaData examMetaData : examMetaDataList) {
            if (formTypeEnum.equals(FormTypeEnum.ADMIT)) {
                buildAdmitSubSections(landingSubSectionDTOList);
            } else if (formTypeEnum.equals(FormTypeEnum.RESULT)) {
                buildResultSubSection(landingSubSectionDTOList);
            } else {
                buildFormSubSections(landingSubSectionDTOList, examMetaData, i);
                i++;
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
    public void buildAdmitSubSections(List<LandingSubSectionDTO> landingSubSectionDTOS) {

        List<AdmitCard> admitCardList = dbMgmtFacade.getLatestAdmitCardList(0,6, AkashConstants.DATE_CREATED);
        int i = 0;
        for (AdmitCard admitCard : admitCardList) {
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();
            landingSubSectionDTO.setCardColor(FormUtil.fetchCardColor(i%4));
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
    }

    //build tox x result list to send to home page
    public void buildResultSubSection(List<LandingSubSectionDTO> landingSubSectionDTOS) {

        //fetch result list
        List<ResultDetails> resultDetailsList = dbMgmtFacade.getResultDetailList(0,6, AkashConstants.DATE_CREATED);

        int i = 0;
        for (ResultDetails resultDetails : resultDetailsList) {
            LandingSubSectionDTO landingSubSectionDTO = new LandingSubSectionDTO();

            landingSubSectionDTO.setCardColor(FormUtil.fetchCardColor(i%4));
            landingSubSectionDTO.setKey("RESULT");
            landingSubSectionDTO.setTitle(resultDetails.getResultName());
            landingSubSectionDTO.setShowDate(DateUtils.getFormatedDate1(resultDetails.getResultDate()));
            landingSubSectionDTO.setExamId(resultDetails.getAppIdRef());
            landingSubSectionDTO.setId(resultDetails.getId());
            landingSubSectionDTO.setShowDateColor(FormUtil.getLastXDaysDateColor(resultDetails.getResultDate()));

            landingSubSectionDTOS.add(landingSubSectionDTO);
            i++;
        }

    }

    public void saveResponseToDb(String responseType, String response) {
        FormExecutorService.responseSaveService.submit(()->
                pushResponseToDb(responseType, response));
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


    //build secondary page response based on type and subType
    public void buildAndSaveResponseToDb(){

        for(FormSubTypeEnum formSubTypeEnum : FormSubTypeEnum.values()){
            if(formSubTypeEnum.equals(FormSubTypeEnum.STATE)){
                //will think latter based on response because to save each state it will have huge data in response table
                continue;
            }
            String responseRes = applicationDbUtil.fetchResponseBasedOnSubType(formSubTypeEnum);
            saveResponseToDb(formSubTypeEnum.name(), responseRes);
        }
    }
}
