package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnsKeyResponseDTO {

    @SerializedName("ans_key_intro")
    AnsKeyIntroDTO ansKeyIntroDTO;

    @SerializedName("ans_key_content")
    AnsKeyContentDTO ansKeyContentDTO;

    @SerializedName("related_admit")
    RelatedFormResponseDTO relatedFormResponseDTO;
}
