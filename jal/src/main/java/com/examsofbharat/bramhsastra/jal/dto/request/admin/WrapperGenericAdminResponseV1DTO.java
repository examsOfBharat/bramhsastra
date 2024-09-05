package com.examsofbharat.bramhsastra.jal.dto.request.admin;


import com.examsofbharat.bramhsastra.jal.dto.ApplicationSeoDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.AdminUserDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.UpcomingFormsAdminResDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WrapperGenericAdminResponseV1DTO {

    @JsonProperty("admin_user_details")
    AdminUserDetailsDTO adminUserDetailsDTO;

    @JsonProperty("generic_v1_details")
    AdminGenericResponseV1 adminGenericResponseV1;

    @JsonProperty("upcoming_details")
    UpcomingFormsAdminResDTO upcomingFormsAdminResDTO;

    @JsonProperty("application_seo_details")
    ApplicationSeoDetailsDTO applicationSeoDetailsDTO;
}
