package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeGradeSectionDTO {
    private String subtitle;
    private String imgUrl;
    private String colorCode;
    private List<LandingSectionDTO> landingSectionDTOS = new ArrayList<>();
}
