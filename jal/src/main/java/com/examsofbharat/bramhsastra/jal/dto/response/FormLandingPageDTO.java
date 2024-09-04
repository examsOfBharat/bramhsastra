package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.dto.HomePageCountDataDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormLandingPageDTO {

    private String engTitle;
    private String hindiTitle;
    private String subTitle;

    @SerializedName("header_count_data")
    private HomePageCountDataDTO homePageCountDataDTO;

    @SerializedName("super_prime_section")
    private SuperPrimeSectionDTO superPrimeSectionDTO;

    @SerializedName("sub_prime_section")
    private SubPrimeSectionDTO subPrimeSectionDTO;

    @SerializedName("prime_section")
    private PrimeSectionDTO primeSectionDTO;

    @SerializedName("grade_section")
    private HomeGradeSectionDTO homeGradeSectionDTO;

    @SerializedName("province_section")
    private HomeProvinceSectionDTO homeProvinceSectionDTO;

    @SerializedName("ans_key_section")
    private HomeAnsKeySectionDTO homeAnsKeySectionDTO;

    @SerializedName("upcoming_section")
    private HomeUpcomingFormDTO homeUpcomingFormDTO;

}
