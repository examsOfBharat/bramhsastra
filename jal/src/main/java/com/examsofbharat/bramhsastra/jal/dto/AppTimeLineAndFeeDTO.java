package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppTimeLineAndFeeDTO {
    String heading;
    int sortIndex;

    @SerializedName("importants_dates")
    ImportantDatesDTO importantDatesDto;

    @SerializedName("fee_details")
    AppFeeDetailsDTO appFeeDetailsDto;

    @SerializedName("age_relaxation")
    AgeRelaxationDTO ageRelaxationDTO;

    @SerializedName("extra_information")
    AgeAndFeeInformation ageAndFeeInformation;
}
