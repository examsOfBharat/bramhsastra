package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecondaryAdmitResponseDTO {
    private String id;
    private String name;
    private String examDate;
    private String examDateColor;
    private String logoUrl;
    private String multiDate;
    private String multiDateColor;
    private String downloadUrl;
}
