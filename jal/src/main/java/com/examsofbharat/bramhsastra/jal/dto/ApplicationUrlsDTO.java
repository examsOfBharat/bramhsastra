package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationUrlsDTO {

    String id;
    String appIdRef;
    String officialWebsite;
    String notification;
    String apply;
    String register;
    String admitCard;
    String result;
    String others;
    Date dateCreated;
    Date dateModified;

}
