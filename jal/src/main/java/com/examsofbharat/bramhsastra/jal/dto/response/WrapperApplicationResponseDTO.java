package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.dto.ApplicationBasicDetailDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WrapperApplicationResponseDTO {

    @JsonProperty("application_form_data")
    EnrichedFormDetailsDTO enrichedFormDetailsDTO;

    @JsonProperty("application_basic_details")
    ApplicationBasicDetailDTO applicationBasicDetailDTO;

}
