package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecondaryResultResponseDTO {
    private String id;
    private String name;
    private String logoUrl;
    private String resultDate;
    private String getResultDateColor;
    private String resultUrl;
}
