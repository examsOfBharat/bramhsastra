package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VacancyEligibilityDTO {
    @JsonProperty("vacancy_details")
    VacancyResDetailsDTO vacancyDetailsDTO;
    @JsonProperty("eligibility_details")
    EligibilityDataDTO eligibilityDataDTO;
}
