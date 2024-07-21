package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LandingSubSectionDTO {
    private String cardColor;
    private String id;
    private String title;
    private String titleColor;
    private String key;
    private String examId;
    private String logoUrl;
    private String showDate;
    private String showDateColor;
    private String examDate;
    private String examDateColor;
    private double totalApplication = -1.0;
    private String applicationColor;
    private String totalVacancy;
    private String vacancyColor;
    private String extraField;
}
