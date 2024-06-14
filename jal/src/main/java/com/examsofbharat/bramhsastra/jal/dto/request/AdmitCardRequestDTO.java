package com.examsofbharat.bramhsastra.jal.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdmitCardRequestDTO {
    private String appIdRef;
    private String admitCardName;
    private Date examDate;
    private String downloadUrl;
    private String heading;
    private String body;
    private String addOn;
}
