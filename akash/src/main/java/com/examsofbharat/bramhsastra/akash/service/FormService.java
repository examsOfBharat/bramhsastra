package com.examsofbharat.bramhsastra.akash.service;

import com.examsofbharat.bramhsastra.akash.utils.UUIDUtil;
import com.examsofbharat.bramhsastra.akash.utils.WebUtils;
import com.examsofbharat.bramhsastra.akash.validator.FormValidator;
import com.examsofbharat.bramhsastra.jal.dto.*;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationContentManager;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FormService {

    @Autowired
    WebUtils webUtils;

    public Response formLandingResponse(){
        return Response.ok().build();
    }

    public Response saveForm(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        if(FormValidator.isValidFormRequest(enrichedFormDetailsDTO)){
            return webUtils.invalidRequest();
        }

        final String examId = UUIDUtil.generateUUID();
        log.info("Saving form with id {}", examId);

        final Date dateCreated = new Date();

        ApplicationFormDTO applicationFormDTO = enrichedFormDetailsDTO.getApplicationFormDTO();
        applicationFormDTO.setId(examId);
        applicationFormDTO.setDateCreated(dateCreated);
        applicationFormDTO.setDateModified(dateCreated);

        ApplicationAgeDetailsDTO applicationAgeDetailsDTO = enrichedFormDetailsDTO.getApplicationAgeDetailsDTO();
        applicationAgeDetailsDTO.setId(UUIDUtil.generateUUID());
        applicationAgeDetailsDTO.setExamIdRef(examId);
        applicationAgeDetailsDTO.setDateCreated(dateCreated);
        applicationAgeDetailsDTO.setDateModified(dateCreated);

        ApplicationFeeDTO applicationFeeDTO = enrichedFormDetailsDTO.getApplicationFeeDTO();
        applicationFeeDTO.setId(UUIDUtil.generateUUID());
        applicationFeeDTO.setExamIdRef(examId);
        applicationFeeDTO.setDateCreated(dateCreated);
        applicationFeeDTO.setDateModified(dateCreated);

        ApplicationUrlsDTO applicationUrlsDTO = enrichedFormDetailsDTO.getApplicationUrlsDTO();
        applicationUrlsDTO.setId(UUIDUtil.generateUUID());
        applicationUrlsDTO.setExamIdRef(examId);
        applicationUrlsDTO.setDateCreated(dateCreated);
        applicationUrlsDTO.setDateModified(dateCreated);


        List<ApplicationContentManagerDTO> applicationContentManagerDTOList = enrichedFormDetailsDTO.getApplicationContentManagerDTO();
        List<ApplicationContentManager> formContentList = new ArrayList<>();

        if(applicationContentManagerDTOList != null && !applicationContentManagerDTOList.isEmpty()){
            formContentList.forEach(formContentManagerDTO -> {
                formContentManagerDTO.setId(UUIDUtil.generateUUID());
                formContentManagerDTO.setDateCreated(dateCreated);
                formContentManagerDTO.setDateModified(dateCreated);
                formContentManagerDTO.setAppIdRef(examId);
            });
        }

        return Response.ok().build();
    }



}
