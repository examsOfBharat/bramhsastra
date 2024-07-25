package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationEligibilityDTO {

    String id;
    String appIdRef;
    String qualification;
    String ageRange;
    String experience;
    String others;
    int sequence;
    Date dateCreated;
    Date dateModified;
}
