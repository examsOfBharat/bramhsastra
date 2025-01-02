package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogHeaderDTO {
    private String title;
    private Date dateModified;
    private String shortTitle;
    private String writer;
    private String tags;
    private List<String> tagsList;
    private String thumbNail;
    private String contentImg;
}
