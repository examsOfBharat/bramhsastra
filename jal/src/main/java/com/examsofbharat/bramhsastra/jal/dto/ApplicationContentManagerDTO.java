package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationContentManagerDTO {

    String id;
    String appIdRef;
    String heading;
    String body;
    @JsonProperty("add_on")
    String addOn;
    Date dateCreated;
    Date dateModified;
}
