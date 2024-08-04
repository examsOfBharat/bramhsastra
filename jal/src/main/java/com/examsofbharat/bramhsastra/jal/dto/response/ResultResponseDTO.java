package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.dto.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultResponseDTO {
    @SerializedName("result_intro")
    ResultIntroDTO resultIntroDTO;

    @SerializedName("result_content")
    ResultContentManagerDTO resultContentManagerDTO;

    @SerializedName("related_admit")
    RelatedFormResponseDTO relatedFormResponseDTO;
}
