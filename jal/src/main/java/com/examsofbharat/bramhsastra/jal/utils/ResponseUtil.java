package com.examsofbharat.bramhsastra.jal.utils;

import com.examsofbharat.bramhsastra.jal.constants.ErrorConstants;
import com.examsofbharat.bramhsastra.jal.dto.response.ErrorResponse;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    public ErrorResponse setErrorResponse(String errorType) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(errorType);
        errorResponse.setErrorTitle(ErrorConstants.getErrorTitleMap().get(errorType));
        errorResponse.setErrorMessage(ErrorConstants.getErrorMsgMap().get(errorType));

        return errorResponse;
    }
}
