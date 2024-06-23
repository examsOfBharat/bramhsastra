package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppTimeLineAndFeeDTO {
    String heading;
    int sortIndex;

    @JsonProperty("importants_dates")
    ImportantDatesDTO importantDatesDto;

    @JsonProperty("fee_details")
    AppFeeDetailsDTO appFeeDetailsDto;

    @JsonProperty("age_relaxation")
    AgeRelaxationDTO ageRelaxationDTO;

    @JsonProperty("extra_information")
    AgeAndFeeInformation ageAndFeeInformation;
}
