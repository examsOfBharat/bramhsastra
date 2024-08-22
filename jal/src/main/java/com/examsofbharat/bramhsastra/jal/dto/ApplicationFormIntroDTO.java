package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationFormIntroDTO {
    String title;
    String subtitle;
    String appId;
    String type;
    String logoUrl;
    String lastDate;
    String lastDateColor;
    String releaseDateTitle;
    String releaseDate;
    String minQualification;
    String vacancy;
    String postedOn;
    String postedOnColor;
    String applyUrl;
    String registerUrl;
    String ageRange;
    private boolean checkEligibility;
    UrlsDetailsDTO urlsDetailsDto;
    String qualificationKey;
    String qualificationValue;
    private String formStatus;
}
