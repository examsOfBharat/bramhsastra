package com.examsofbharat.bramhsastra.jal.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterDTO {
    String firstName;
    String lastName;
    String userName;
    String passWord;
    String phoneNumber;
}
