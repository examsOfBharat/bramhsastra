package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.dto.ApplicationContentManagerDTO;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationSeoDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.BlogHeaderDTO;
import com.examsofbharat.bramhsastra.jal.dto.BlogUpdatesDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogResponseDTO {

    BlogHeaderDTO blogHeader;
    List<ApplicationContentManagerDTO> contentManagerDTOList;
    List<BlogUpdatesDTO> blogUpdatesList;
    ApplicationSeoDetailsDTO seoDetailsDTO;
}
