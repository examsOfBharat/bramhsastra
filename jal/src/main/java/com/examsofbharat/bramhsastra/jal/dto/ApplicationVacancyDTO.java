package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationVacancyDTO {

    String id;
    double general;
    double st;
    double sc;
    double obc;
    double female;
    @JsonProperty("ex_army")
    double exArmy;
    @JsonProperty("total_vacancy")
    double totalVacancy;
    String information;

}
