package com.examsofbharat.bramhsastra.jal.dto;

import com.examsofbharat.bramhsastra.jal.dto.response.UrlManagerDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportantButtonDetailsDTO {

    private String notificationUrl;
    private String admitCardId;
    private String resultId;
    private String syllabusUrl;
    private String ansKeyUrl;
    private String other1;
    private String cutOffUrl;
    private List<UrlManagerDTO> pyqList;
    private List<UrlManagerDTO> impUrlList;
    private String officialWebsite;

}
