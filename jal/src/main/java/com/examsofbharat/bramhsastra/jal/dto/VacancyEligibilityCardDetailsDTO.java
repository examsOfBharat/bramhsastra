package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VacancyEligibilityCardDetailsDTO {
    String title;
    int sortIndex;
    @SerializedName("vacancy_eligibility_data_list")
    List<VacancyEligibilityDataDTO> vacancyEligibilityDataList;

    @SerializedName("extra_info")
    VacancyEligibilityAdditionalInfo vacancyEligibilityAdditionalInfo;
}
