package com.examsofbharat.bramhsastra.jal.dto.request;


import com.examsofbharat.bramhsastra.jal.dto.ApplicationContentManagerDTO;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationSeoDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.BlogHeaderDTO;
import com.examsofbharat.bramhsastra.jal.dto.BlogUpdatesDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogRequestDTO {

    BlogHeaderDTO blogHeader;
    BlogUpdatesDTO blogUpdates;
    ApplicationContentManagerDTO contentManagerDTO;
    ApplicationSeoDetailsDTO seoDetailsDTO;
}
