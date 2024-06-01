package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationAgeDetailsDTO {

    String id;
    String examIdRef;
    @JsonProperty("min_age")
    double minAge;
    double general;
    double st;
    double sc;
    double obc;
    double female;
    @JsonProperty("ex_army")
    double exArmy;
    String information;
    Date dateCreated;
    Date dateModified;
}
