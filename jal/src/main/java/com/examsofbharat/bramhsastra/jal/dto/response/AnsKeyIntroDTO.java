package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnsKeyIntroDTO {
    private String appIdRef;
    private String ansName;
    private String downloadUrl;
    private String ansDate;
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
