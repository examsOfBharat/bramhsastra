package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LandingSectionDTO {
    private String title;
    private String titleColor;
    private FormTypeEnum type;
    private int sortIndex;
    private boolean viewAll;
    private List<LandingSubSectionDTO> subSections = new ArrayList<>();
}
