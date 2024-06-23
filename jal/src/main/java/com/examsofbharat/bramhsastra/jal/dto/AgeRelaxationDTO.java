package com.examsofbharat.bramhsastra.jal.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgeRelaxationDTO {
    String title;
    String generalAge;
    String obcAge;
    String stAge;
    String scAge;
    String femaleAge;
    String exArmy;
    String information;
    String cardColor;
}
