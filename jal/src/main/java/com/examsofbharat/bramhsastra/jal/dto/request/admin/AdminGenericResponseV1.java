package com.examsofbharat.bramhsastra.jal.dto.request.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminGenericResponseV1 {

    private String appIdRef;
    private String title;
    private Date showDate;
    private String downloadUrl;
    private Date updateDate;
    private String pageType;

    //content section
    private String heading;
    private String body;
    private String addOn;
}
