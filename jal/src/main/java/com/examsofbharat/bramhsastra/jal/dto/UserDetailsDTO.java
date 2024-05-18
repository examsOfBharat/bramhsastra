package com.examsofbharat.bramhsastra.jal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailsDTO {
    private String id;
    private String firstName;
    private String secondName;
    private String passWord;
    private String emailId;
    private String enableFlag;
    private String userRole;
    private String otp;
    private int attempts;
    private String status;
    private String phoneNumber;
    private Date dateCreated;
    private Date dateModified;
}
