package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeTotalCountDTO {
    private int totalForms;
    private int todayForm;
    private String totalVacancy;
    private int todayVacancy;
}
