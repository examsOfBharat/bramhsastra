package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppFeeDetailsDTO {
    String title;
    String general;
    String obc;
    String st;
    String sc;
    String pwd;
    String female;
    String exArmy;
    String cardColor;
}
