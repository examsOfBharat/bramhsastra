package com.examsofbharat.bramhsastra.jal.dto.request;


import com.examsofbharat.bramhsastra.jal.dto.ApplicationSeoDetailsDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WrapperAnsKeyAdminResDTO {

    @JsonProperty("admin_user_details")
    AdminUserDetailsDTO adminUserDetailsDTO;

    @JsonProperty("ans_key_details")
    AnsKeyAdminResDTO ansKeyAdminResDTO;

    @JsonProperty("application_seo_details")
    ApplicationSeoDetailsDTO applicationSeoDetailsDTO;

}
