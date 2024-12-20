package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LandingSubSectionDTO {
    private String cardColor;
    private String id;
    private String title;
    private String titleColor;
    private String formType;
    private String key;
    private String examId;
    private String logoUrl;
    private String showDateTitle;
    private String showDate;
    private Date sortDate;
    private String showDateColor;
    private String examDate;
    private String examDateColor;
    private double totalApplication = -1.0;
    private String applicationColor;
    private String vacancyTitle;
    private String totalVacancy;
    private String vacancyColor;
    private String extraField;
    private String formLogoUrl;
    private String formStatus;
    private String urlTitle;
}
