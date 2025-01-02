package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeBlogDataDTO {
    private String id;
    private String title;
    private String shortTitle;
    private String blogId;
    private String type;
    private Date dateModified;
    private String writer;
    private String thumbNail;
}
