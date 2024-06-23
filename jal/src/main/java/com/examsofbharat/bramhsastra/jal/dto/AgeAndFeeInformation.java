package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgeAndFeeInformation {
    private String feeInfo;
    private String ageInfo;
    private String dateInfo;
}
