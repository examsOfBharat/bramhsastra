package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportantDatesDTO {
    String title;
    String startDate;
    String lastDate;
    String lastDateColor;
    String examDate;
    String examDateColor;
    String cardColor;
    String lastPayDate;
    String lastPayDateColor;
}
