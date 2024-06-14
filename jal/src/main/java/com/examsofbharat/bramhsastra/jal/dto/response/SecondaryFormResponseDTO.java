package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecondaryFormResponseDTO {
    private String id;
    private String name;
    private int minAge;
    private int vacancy;
    private String vacancyColor;
    private String lastDate;
    private String lastDateColor;
    private String minQualification;
    private String formStartDate;
    private String logoUrl;
}
