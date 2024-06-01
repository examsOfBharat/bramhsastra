package com.examsofbharat.bramhsastra.akash.service;

import com.examsofbharat.bramhsastra.akash.utils.WebUtils;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
import com.examsofbharat.bramhsastra.jal.dto.*;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.entity.*;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.examsofbharat.bramhsastra.jal.constants.ErrorConstants.DATA_NOT_FOUND;

@Component
@Service
@Slf4j
public class ApplicationClientService {

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    @Autowired
    WebUtils webUtils;

//    private static final ModelMapper modelMapper = new ModelMapper();

    ObjectMapper objectMapper = new ObjectMapper();

    public Response buildAndGetApplication(String appId){
        if(StringUtil.isEmpty(appId)){
            return webUtils.invalidRequest();
        }

        EnrichedFormDetailsDTO enrichedFormDetailsDTO = getEnrichedFormDetails(appId);
        if(Objects.isNull(enrichedFormDetailsDTO)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, DATA_NOT_FOUND);
        }

        return Response.ok(enrichedFormDetailsDTO).build();
    }

    public EnrichedFormDetailsDTO getEnrichedFormDetails(String appId) {

        ApplicationForm applicationForm = dbMgmtFacade.getApplicationForm(appId);

        if(Objects.nonNull(applicationForm)){
            log.info("Populating enriched form details appId::{}", appId);
            return enrich(applicationForm, appId);
        }
        return null;
    }

    public EnrichedFormDetailsDTO enrich(ApplicationForm applicationForm, String appId) {
        EnrichedFormDetailsDTO enrichedFormDetailsDTO = new EnrichedFormDetailsDTO();

        enrichedFormDetailsDTO.setApplicationFormDTO(objectMapper.convertValue(applicationForm, ApplicationFormDTO.class));

        ApplicationFeeDatails applicationFeeDatails = dbMgmtFacade.getApplicationFeeDetails(appId);
        enrichedFormDetailsDTO.setApplicationFeeDTO(objectMapper.convertValue(applicationFeeDatails, ApplicationFeeDTO.class));

        ApplicationAgeDetails applicationAgeDetails = dbMgmtFacade.getApplicationAgeDetails(appId);
        enrichedFormDetailsDTO.setApplicationAgeDetailsDTO(objectMapper.convertValue(applicationAgeDetails, ApplicationAgeDetailsDTO.class));

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


        return enrichedFormDetailsDTO;
    }
}
