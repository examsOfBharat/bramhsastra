package com.examsofbharat.bramhsastra.jal.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgeRelaxationDTO {
    String title;
    int generalAge;
    int obcAge;
    int stAge;
    int scAge;
    int femaleAge;
    int exArmy;
    String information;
    String cardColor;
}
