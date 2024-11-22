package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    double ews;
    double pwd;
    String information;
    Date lastPaymentDate;
    String correctionDate;
    String examDate;
    Date dateCreated;
    Date dateModified;
}
