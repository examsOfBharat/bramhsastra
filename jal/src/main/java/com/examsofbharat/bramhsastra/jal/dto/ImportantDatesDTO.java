package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportantDatesDTO {
    String title;
    String cardColor;
    List<DateDetailsDTO> dateDetails = new ArrayList<>();
}
