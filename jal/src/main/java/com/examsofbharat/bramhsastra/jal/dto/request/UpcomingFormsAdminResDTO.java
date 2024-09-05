package com.examsofbharat.bramhsastra.jal.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpcomingFormsAdminResDTO {
    private String title;
    private String type;
    private Date comingDate;
    private String appIdRef;
}
