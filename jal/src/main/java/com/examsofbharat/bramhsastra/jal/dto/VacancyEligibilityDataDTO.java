package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VacancyEligibilityDataDTO {
    String department;
    String cardColor;

    @SerializedName("vacancy_details")
    VacancyResDetailsDTO vacancyDetailsDTO;
    @SerializedName("eligibility_details")
    EligibilityDataDTO eligibilityDataDTO;
}
