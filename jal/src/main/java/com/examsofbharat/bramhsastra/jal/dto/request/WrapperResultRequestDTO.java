package com.examsofbharat.bramhsastra.jal.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WrapperResultRequestDTO {

    @JsonProperty("admin_user_details")
    AdminUserDetailsDTO adminUserDetailsDTO;

    @JsonProperty("result_details")
    ResultRequestDTO resultRequestDTO;
}