package com.examsofbharat.bramhsastra.akash.factory.componentParser;

import com.examsofbharat.bramhsastra.jal.dto.FormViewResponseDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;

public interface ContentParser {

    void parseResponse(ComponentRequestDTO componentRequestDTO, FormViewResponseDTO formViewResponseDTO, int sortIndex);
}
