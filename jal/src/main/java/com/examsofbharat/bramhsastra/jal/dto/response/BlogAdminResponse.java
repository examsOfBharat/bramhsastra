package com.examsofbharat.bramhsastra.jal.dto.response;

import com.examsofbharat.bramhsastra.jal.dto.ApplicationContentManagerDTO;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationSeoDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.BlogHeaderDTO;
import com.examsofbharat.bramhsastra.jal.dto.BlogUpdatesDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.AdminUserDetailsDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogAdminResponse {

    BlogHeaderDTO blogHeader;
    List<BlogUpdatesDTO> blogUpdatesList;
    List<ApplicationContentManagerDTO> contentManagerDTOList;

    @JsonProperty("seoDetailsDTO")
    ApplicationSeoDetailsDTO applicationSeoDetailsDTO;

    @JsonProperty("admin_user_details")
    AdminUserDetailsDTO adminUserDetailsDTO;
}
