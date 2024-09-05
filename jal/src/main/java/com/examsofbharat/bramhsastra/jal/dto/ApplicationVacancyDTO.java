package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationVacancyDTO {

    String id;
    String appIdRef;
    String department;
    int general;
    int st;
    int sc;
    int obc;
    int ews;
    int female;
    int exArmy;
    int pwd;
    int totalVacancy;
    String information;

    String qualification;
    String ageRange;
    String experience;

    int sequence;
    Date dateCreated;
    Date dateModified;

}
