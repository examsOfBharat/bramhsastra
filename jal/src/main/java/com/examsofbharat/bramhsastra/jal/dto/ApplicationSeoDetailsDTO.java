package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationSeoDetailsDTO {

    private String id;
    private String appIdRef;
    private String title;
    private String keywords;
    private String descreption;
    private Date dateCreated;
    private Date dateModified;
}
