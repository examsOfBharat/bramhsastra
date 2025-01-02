package com.examsofbharat.bramhsastra.jal.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrendGraphDTO {
    private String title;
    private List<String> trendType;
    private List<TrendGraphDataDTO> trendGraphDataList = new ArrayList<>();

}
