package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecondaryPageDataDTO {
    private String cardColor;
    private String id;
    private String title;
    private String titleColor;
    private String subType;
    private String appIdRef;
    private String releaseDate;
    private String releaseDateColor;
    private String lastDate;
    private String lastDateColor;
    private String examDate;
    private String examDateColor;
    private double totalApplication;
    private String applicationColor;
    private double totalVacancy;
    private String vacancyColor;
    private String extraField;
    private String pageType;
    private String status;
}
