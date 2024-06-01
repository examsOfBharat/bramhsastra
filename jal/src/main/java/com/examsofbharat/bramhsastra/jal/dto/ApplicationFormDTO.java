package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationFormDTO {

    String id;
    @JsonProperty("exam_name")
    String examName;
    @JsonProperty("exam_date")
    Date startDate;
    @JsonProperty("end_date")
    Date endDate;
    @JsonProperty("min_age")
    int minAge;
    @JsonProperty("min_qualification")
    String minQualification;
    String sector;
    String province;
    String grade;
    String gender;
    Date dateCreated;
    Date dateModified;
}
