package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubPrimeSectionDTO {

    @SerializedName("home_admit")
    HomeAdmitCardSection homeAdmitCardSection;

    @SerializedName("home_result")
    HomeResultDetailsDTO homeResultDetailsDTO;
}
