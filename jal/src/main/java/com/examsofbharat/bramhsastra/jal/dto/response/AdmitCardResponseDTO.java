package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.dto.AdmitCardDTO;
import com.examsofbharat.bramhsastra.jal.dto.AdmitCardIntroDTO;
import com.examsofbharat.bramhsastra.jal.dto.AdmitContentManagerDTO;
import com.examsofbharat.bramhsastra.jal.dto.RelatedFormDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdmitCardResponseDTO {
    @SerializedName("admit_card_intro")
    AdmitCardIntroDTO admitCardIntroDTO;

    @SerializedName("admit_card_content")
    AdmitContentManagerDTO admitContentManagerDTO;

    @SerializedName("related_admit")
    RelatedFormResponseDTO relatedFormResponseDTO;
}
