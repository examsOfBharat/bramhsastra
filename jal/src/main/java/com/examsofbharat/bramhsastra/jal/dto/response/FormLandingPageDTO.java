package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.dto.HomePageCountDataDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormLandingPageDTO {

    private String engTitle;
    private String hindiTitle;
    private String subTitle;
    @JsonProperty("header_count_data")
    private HomePageCountDataDTO homePageCountDataDTO;
    private List<LandingSectionDTO> landingSectionDTOS = new ArrayList<>();
}
