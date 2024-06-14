package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.dto.AdmitCardDTO;
import com.examsofbharat.bramhsastra.jal.dto.AdmitContentManagerDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdmitCardResponseDTO {
    AdmitCardDTO admitCardDTO;
    AdmitContentManagerDTO admitContentManagerDTO;
}
