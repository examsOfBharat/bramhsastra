package com.examsofbharat.bramhsastra.jal.dto;

import com.examsofbharat.bramhsastra.jal.dto.response.RelatedFormResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormViewResponseDTO {

    @JsonProperty("application_intro_details")
    ApplicationFormIntroDTO applicationFormIntroDTO;

    @JsonProperty("vacancy_eligibility_card_details")
    VacancyEligibilityCardDetailsDTO vacancyEligibilityCardDetailsDTO;

    @JsonProperty("age_and_fee_details")
    AppTimeLineAndFeeDTO appTimeLineAndFeeDTO;

    @JsonProperty("form_content_data")
    List<ApplicationContentManagerDTO> applicationContentManagers;

    @JsonProperty("related_form_list")
    RelatedFormResponseDTO relatedFormResponseDTO;

    @JsonProperty("important_button")
    ImportantButtonDetailsDTO importantButtonDetailsDTO;
}
