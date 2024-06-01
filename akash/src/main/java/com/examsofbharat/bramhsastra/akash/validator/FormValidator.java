package com.examsofbharat.bramhsastra.akash.validator;

import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;

import java.util.Objects;

public class FormValidator {
    public static boolean isValidFormRequest(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        return !Objects.isNull(enrichedFormDetailsDTO.getApplicationFormDTO())
                && !Objects.isNull(enrichedFormDetailsDTO.getApplicationUrlsDTO())
                && !Objects.isNull(enrichedFormDetailsDTO.getApplicationFeeDTO())
                && !Objects.isNull(enrichedFormDetailsDTO.getApplicationVacancyDTOS())
                && !Objects.isNull(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO())
                && !Objects.isNull(enrichedFormDetailsDTO.getApplicationContentManagerDTO());
    }
}
