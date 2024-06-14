package com.examsofbharat.bramhsastra.jal.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecondaryPageRequestDTO {
    private Integer page;
    private Integer size;

    //ADMIT, SECTOR_BASED, QUALIFICATION_BASED etc
    private String requestType;
    //ADMIT,RESULT,TENTH,BANKING etc
    private String requestSubType;
}
