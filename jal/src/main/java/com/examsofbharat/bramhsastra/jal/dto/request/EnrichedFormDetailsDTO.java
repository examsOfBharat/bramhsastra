package com.examsofbharat.bramhsastra.jal.dto.request;

import com.examsofbharat.bramhsastra.jal.dto.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnrichedFormDetailsDTO {

    @JsonProperty("admin_user_details")
    AdminUserDetailsDTO adminUserDetailsDTO;

    @JsonProperty("application_form")
    ApplicationFormDTO applicationFormDTO;

    @JsonProperty("application_fee_details")
    ApplicationFeeDTO applicationFeeDTO;

    @JsonProperty("application_url")
    ApplicationUrlsDTO applicationUrlsDTO;

    @JsonProperty("application_age_details")
    ApplicationAgeDetailsDTO applicationAgeDetailsDTO;

    @JsonProperty("application_vacancy_details")
    List<ApplicationVacancyDTO> applicationVacancyDTOS;

    @JsonProperty("application_content_manager")
    List<ApplicationContentManagerDTO> applicationContentManagerDTO;

    @JsonProperty("application_eligibility")
    List<ApplicationEligibilityDTO> applicationEligibilityDTOS;

    @JsonProperty("application_seo_details")
    ApplicationSeoDetailsDTO applicationSeoDetailsDTO;
}
