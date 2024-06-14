package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationFeeDTO {

    String id;
    String appIdRef;
    double defaultFee;
    double general;
    double st;
    double sc;
    double obc;
    double female;
    double exArmy;
    String information;
    Date lastPaymentDate;
    Date dateCreated;
    Date dateModified;
}
