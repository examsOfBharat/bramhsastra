package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecondaryPageDataDTO {
    private String cardColor;
    private String id;
    private String title;
    private String urlTitle;
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
    private String totalVacancy;
    private String vacancyColor;
    private String extraField;
    private String pageType;
    private String status;
    private String imageUrl;
    private boolean newFlag;
    private String formStatus;
    private List<String> qualificationList;
    private String expVacancy;
    private String logoUrl;
}
