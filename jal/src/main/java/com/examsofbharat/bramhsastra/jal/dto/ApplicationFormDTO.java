package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationFormDTO {

    String id;
    String appIdRef;
    String examName;
    Date startDate;
    Date endDate;
    int minAge;
    int totalVacancy;
    String minQualification;
    String sectors;
    String province;
    String state;
    String grade;
    String gender;
    String admitId;
    String resultId;
    String answerKeyUrl;
    String answerDate;
    Date dateCreated;
    Date dateModified;
}
