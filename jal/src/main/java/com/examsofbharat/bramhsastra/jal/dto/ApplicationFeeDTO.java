package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationFeeDTO {

    String id;
    String examIdRef;
    @JsonProperty("default_fee")
    double defaultFee;
    double general;
    double st;
    double sc;
    double obc;
    double female;
    @JsonProperty("ex_army")
    double exArmy;
    String information;
    @JsonProperty("last_payment_date")
    Date lastPaymentDate;
    Date dateCreated;
    Date dateModified;
}
