package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationUrlsDTO {

    String id;
    String examIdRef;
    @JsonProperty("official_website")
    String officialWebsite;
    String notification;
    String others;
    Date dateCreated;
    Date dateModified;

}
