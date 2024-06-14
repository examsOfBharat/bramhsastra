package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UrlsDetailsDTO {
    String officialWebsite;
    String notification;
    String apply;
    String register;
    String admitCard;
    String result;
    String others;
}
