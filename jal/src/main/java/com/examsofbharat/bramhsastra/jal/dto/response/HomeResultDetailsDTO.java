package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeResultDetailsDTO {
    private String title;
    private String cardColor;
    private String lastUpdate;
    private String updateDateColor;
    private String lastReleaseCountTitle;
    private String lastResultReleaseCount;
    private String type;
    private List<LandingSectionDTO> landingSectionDTOS = new ArrayList<>();
}
