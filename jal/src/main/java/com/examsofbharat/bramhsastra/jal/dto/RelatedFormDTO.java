package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedFormDTO {
    private String id;
    private String name;
    private String releaseDate;
    private String cardColor;
    private String pageType;
}
