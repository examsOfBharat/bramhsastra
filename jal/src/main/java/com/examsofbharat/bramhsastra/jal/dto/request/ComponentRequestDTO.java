package com.examsofbharat.bramhsastra.jal.dto.request;

import com.examsofbharat.bramhsastra.jal.dto.ApplicationNameDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentRequestDTO {
    EnrichedFormDetailsDTO enrichedFormDetailsDTO;
    ApplicationNameDTO applicationNameDTO;
}
