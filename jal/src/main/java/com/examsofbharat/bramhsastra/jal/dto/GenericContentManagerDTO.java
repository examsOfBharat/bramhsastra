package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericContentManagerDTO {

    private String id;
    private String formIdRef;
    private String heading;
    private String body;
    private String addOn;
    private Date dateCreated;
    private Date dateModified;
}
