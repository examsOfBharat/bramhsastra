package com.examsofbharat.bramhsastra.jal.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpcomingFormsAdminResDTO {
    private String title;
    private String type;
    private String comingDate;
    private String appIdRef;
    private String qualificationList;
    private String expVacancy;

}
