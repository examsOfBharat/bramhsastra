package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeAnsKeySectionDTO {
    private String title;
    private String subTitle;
    private String ansKeyType;
    private String lastResultReleaseCount;
    private String updatedDate;
    private String cardColor;
    private List<String> resultNameList = new ArrayList<>();
}
