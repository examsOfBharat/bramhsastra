package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeAdmitCardSection {
    private String title;
    private String cardColor;
    private String lastUpdate;
    private int totalApplication;
    private String updateDateColor;
    private String lastReleaseCountTitle;
    private String lastAdmitReleaseCount;
    private String type;
    private List<LandingSectionDTO> landingSectionDTOS = new ArrayList<>();
}
