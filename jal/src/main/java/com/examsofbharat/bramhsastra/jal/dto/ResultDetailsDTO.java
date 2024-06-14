package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultDetailsDTO {
    private String id;
    private String appIdRef;
    private String resultName;
    private Date resultDate;
    private String resultUrl;
    private Date dateCreated;
    private Date dateModified;
}
