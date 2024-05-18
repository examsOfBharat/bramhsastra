package com.examsofbharat.bramhsastra.jal.dto.response;

import lombok.Data;

@Data
public class WebResponse {
    private String status;
    private ErrorResponse errorResponse;
    private String otpId;
    private String token;
    private String userId;
    private String userType;
}
