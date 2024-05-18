package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OwnerLandingResponseDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String registeredTime;
}
