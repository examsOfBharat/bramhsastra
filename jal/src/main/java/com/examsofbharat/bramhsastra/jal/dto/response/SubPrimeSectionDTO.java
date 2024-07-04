package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubPrimeSectionDTO {

    @JsonProperty("home_admit")
    HomeAdmitCardSection homeAdmitCardSection;

    @JsonProperty("home_result")
    HomeResultDetailsDTO homeResultDetailsDTO;
}
