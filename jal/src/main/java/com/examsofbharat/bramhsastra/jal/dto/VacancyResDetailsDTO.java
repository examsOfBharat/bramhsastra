package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VacancyResDetailsDTO {
    int general;
    int obc;
    int sc;
    int st;
    int female;
    int exArmy;
    int totalVacancy;
}
