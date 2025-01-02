package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogUpdatesDTO {
    private String refId;
    private String title;
    private Date dateCreated;
    private Date dateModified;
    private Integer urlFlag;
    private Integer sequence;
    private String value;
    private String titleValue;
}
