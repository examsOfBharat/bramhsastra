package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.dto.HomePageCountDataDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormLandingPageDTO {

    private String engTitle;
    private String hindiTitle;
    private String subTitle;

    @JsonProperty("header_count_data")
    private HomePageCountDataDTO homePageCountDataDTO;

    @JsonProperty("super_prime_section")
    private SuperPrimeSectionDTO superPrimeSectionDTO;

    @JsonProperty("sub_prime_section")
    private SubPrimeSectionDTO subPrimeSectionDTO;

    @JsonProperty("prime_section")
    private PrimeSectionDTO primeSectionDTO;

    @JsonProperty("grade_section")
    HomeGradeSectionDTO homeGradeSectionDTO;

    @JsonProperty("province_section")
    HomeProvinceSectionDTO homeProvinceSectionDTO;

    @JsonProperty("ans_key_section")
    HomeAnsKeySectionDTO homeAnsKeySectionDTO;


}
