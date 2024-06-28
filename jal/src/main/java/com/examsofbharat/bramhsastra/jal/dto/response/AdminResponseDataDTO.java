package com.examsofbharat.bramhsastra.jal.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminResponseDataDTO {
    private String id;
    private String responseType;
    private String status;
    private String adminId;
    private String admin_name;
    private String approverId;
}
