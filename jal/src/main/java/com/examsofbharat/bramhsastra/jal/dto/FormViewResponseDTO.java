package com.examsofbharat.bramhsastra.jal.dto;

import com.examsofbharat.bramhsastra.jal.dto.response.RelatedFormResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormViewResponseDTO {

    @SerializedName("application_intro_details")
    ApplicationFormIntroDTO applicationFormIntroDTO;

    @SerializedName("vacancy_eligibility_card_details")
    VacancyEligibilityCardDetailsDTO vacancyEligibilityCardDetailsDTO;

    @SerializedName("age_and_fee_details")
    AppTimeLineAndFeeDTO appTimeLineAndFeeDTO;

    @SerializedName("form_content_data")
    List<ApplicationContentManagerDTO> applicationContentManagers;

    @SerializedName("related_form_list")
    RelatedFormResponseDTO relatedFormResponseDTO;

    @SerializedName("important_button")
    ImportantButtonDetailsDTO importantButtonDetailsDTO;
}
