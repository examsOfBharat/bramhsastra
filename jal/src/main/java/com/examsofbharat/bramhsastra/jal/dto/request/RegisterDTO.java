package com.examsofbharat.bramhsastra.jal.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterDTO {
    String firstName;
    String lastName;
    @NonNull
    String userName;
    @NonNull
    String passWord;
    String phoneNumber;
    String userRole;
}
