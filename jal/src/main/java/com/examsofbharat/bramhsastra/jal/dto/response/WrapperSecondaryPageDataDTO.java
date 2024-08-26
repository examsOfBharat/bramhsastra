package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.dto.SecondPageSeoDetailsDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WrapperSecondaryPageDataDTO {
    String title;

    //SEO friendly variables
    @SerializedName("second_page_seo")
    SecondPageSeoDetailsDTO seoDetailsDTO;

    List<SecondaryPageDataDTO> formList = new ArrayList<>();
}
