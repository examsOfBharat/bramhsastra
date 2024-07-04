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
    String releaseDate;
    String minQualification;
    int vacancy;
    String postedOn;
    String postedOnColor;
    String applyUrl;
    String registerUrl;
    String ageRange;
    UrlsDetailsDTO urlsDetailsDto;
}