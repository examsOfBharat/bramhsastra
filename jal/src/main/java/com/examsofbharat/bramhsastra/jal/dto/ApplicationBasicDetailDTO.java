package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationBasicDetailDTO {
    String appName;
    String appLogo;
    String lastDate;
    String lastDateColor;
    String daysPosted;
    String daysPostedColor;
}
