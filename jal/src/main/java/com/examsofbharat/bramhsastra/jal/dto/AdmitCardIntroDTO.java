package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdmitCardIntroDTO {
    private String appIdRef;
    private String admitCardName;
    private Date examDate;
    private String downloadUrl;
    private String releaseDate;
    private String examDateValue;
    private String notificationUrl;
    private String logoUrl;
    private String subtitle;
    private String postedOn;
    private String postedOnColor;

    //SEO related details
    private String seoTitle;
    private String seoDescription;
    private String seoKeywords;
    private String shareLogoUrl;
}
