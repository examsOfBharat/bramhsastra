package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportantButtonDetailsDTO {

    private String notificationUrl;
    private String admitCardId;
    private String resultId;
    private String syllabusUrl;
    private String ansKeyUrl;
    private String other1;
    private String other2;

}
