package com.examsofbharat.bramhsastra.jal.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnsKeyAdminResDTO {
    private String appIdRef;
    private String ansKeyName;
    private Date ansKeyDate;
    private String ansKeyUrl;
    private String heading;
    private String body;
    private String addOn;
}
