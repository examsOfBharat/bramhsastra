package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.dto.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultResponseDTO {
    @JsonProperty("result_intro")
    ResultIntroDTO resultIntroDTO;

    @JsonProperty("result_content")
    ResultContentManagerDTO resultContentManagerDTO;

    @JsonProperty("related_admit")
    RelatedFormResponseDTO relatedFormResponseDTO;
}
