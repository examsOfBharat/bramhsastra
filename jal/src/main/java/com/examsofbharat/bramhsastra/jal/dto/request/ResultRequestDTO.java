package com.examsofbharat.bramhsastra.jal.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultRequestDTO {
    private String appIdRef;
    private String resultName;
    private Date resultDate;
    private String resultUrl;
    private String heading;
    private String body;
    private String addOn;
}
