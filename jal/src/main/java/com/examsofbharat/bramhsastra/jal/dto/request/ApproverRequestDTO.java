package com.examsofbharat.bramhsastra.jal.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproverRequestDTO {
    private String approverId;
    private String responseId;
    private String status;
    private String remark;
    private String formType;
}
