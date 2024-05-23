package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LandingSubSectionDTO {
    private String title;
    private String key;
    private String examId;
    private String logoUrl;
    private String showDate;
    private String showDateColor;
    private String examDate;
    private String examDateColor;
    private double totalApplication;
    private String applicationColor;
    private double totalVacancy;
    private String vacancyColor;
    private String extraField;
}
