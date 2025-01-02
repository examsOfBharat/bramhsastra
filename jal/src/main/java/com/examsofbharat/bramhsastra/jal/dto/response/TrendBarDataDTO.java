package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrendBarDataDTO {
    private String key;
    private int value;
}
