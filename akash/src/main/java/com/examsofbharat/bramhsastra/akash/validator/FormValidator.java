package com.examsofbharat.bramhsastra.akash.validator;

import com.examsofbharat.bramhsastra.jal.dto.request.EligibilityCheckRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.SecondaryPageRequestDTO;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

public class FormValidator {
    public static boolean isValidFormRequest(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        return Objects.nonNull(enrichedFormDetailsDTO)
                && Objects.nonNull(enrichedFormDetailsDTO.getApplicationFormDTO())
                && Objects.nonNull(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO())
                && Objects.nonNull(enrichedFormDetailsDTO.getApplicationFeeDTO())
                && Objects.nonNull(enrichedFormDetailsDTO.getApplicationUrlsDTO())
                && !CollectionUtils.isEmpty(enrichedFormDetailsDTO.getApplicationVacancyDTOS())
                && !CollectionUtils.isEmpty(enrichedFormDetailsDTO.getApplicationContentManagerDTO());
    }

    public static boolean isValidSecondaryRequest(SecondaryPageRequestDTO secondaryPageRequestDTO){
        return Objects.nonNull(secondaryPageRequestDTO)
                && Objects.nonNull(secondaryPageRequestDTO.getPage())
                && Objects.nonNull(secondaryPageRequestDTO.getSize())
                && !StringUtil.isEmpty(secondaryPageRequestDTO.getRequestType());
    }

    public static boolean isValidEligibilityRequest(EligibilityCheckRequestDTO eligibilityCheckRequestDTO){
        return Objects.nonNull(eligibilityCheckRequestDTO)
                && StringUtil.notEmpty(eligibilityCheckRequestDTO.getAppId())
                && Objects.nonNull(eligibilityCheckRequestDTO.getDob());
    }
}
