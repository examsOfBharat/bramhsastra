package com.examsofbharat.bramhsastra.jal.dto;

import com.examsofbharat.bramhsastra.jal.dto.response.UrlManagerDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationUrlsDTO {

    String id;
    String appIdRef;
    String officialWebsite;
    String notification;
    String apply;
    String register;
    String syllabus;
    String ansKey;
    List<UrlManagerDTO> urlList;
    Date dateCreated;
    Date dateModified;
}
