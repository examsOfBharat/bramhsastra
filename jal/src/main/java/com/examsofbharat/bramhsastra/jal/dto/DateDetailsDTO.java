package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DateDetailsDTO {
    private String title;
    private String date;
    private String dateColor;
    private String timeBound;
}
