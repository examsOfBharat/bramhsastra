package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdmitCardDTO {
    private String id;
    private String appIdRef;
    private String admitCardName;
    private Date examDate;
    private String downloadUrl;
    private String releaseDate;
    private String examDateValue;
}
