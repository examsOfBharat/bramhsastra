package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericResponseV1DTO {

    private String id;
    private String appIdRef;
    private String title;
    private String downloadUrl;
    private Date dateCreated;
    private Date dateModified;
    private Date showDate;
    private Date updatedDate;
}
