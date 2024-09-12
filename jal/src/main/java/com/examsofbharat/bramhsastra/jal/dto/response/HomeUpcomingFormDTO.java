package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeUpcomingFormDTO {
    private String title;
    private String pageType;
    private String cardColor;
    private String lastReleasedCount;
}
