package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.dto.ResultContentManagerDTO;
import com.examsofbharat.bramhsastra.jal.dto.ResultDetailsDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultResponseDTO {
    ResultDetailsDTO resultDetailsDTO;
    ResultContentManagerDTO resultContentManagerDTO;
}
