package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationAgeDetailsDTO {

    String id;
    String appIdRef;
    int minAge;
    int maxAge;
    int generalAge;
    int stAge;
    int scAge;
    int obcAge;
    int femaleAge;
    int exArmy;
    int ews;
    int pwd;
    String information;
    private Date maxNormalDob;
    private Date minNormalDob;
    private String extraDateDetails;
    Date dateCreated;
    Date dateModified;
}
