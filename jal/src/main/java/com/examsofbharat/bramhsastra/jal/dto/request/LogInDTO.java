package com.examsofbharat.bramhsastra.jal.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogInDTO {

    String userName;
    String passWord;
    boolean otpFlag;
    String otp;
    String userRole;
}
