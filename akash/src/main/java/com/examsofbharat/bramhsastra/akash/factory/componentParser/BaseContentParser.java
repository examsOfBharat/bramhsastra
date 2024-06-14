package com.examsofbharat.bramhsastra.akash.factory.componentParser;

import com.examsofbharat.bramhsastra.jal.dto.FormViewResponseDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class BaseContentParser implements ContentParser{

    @Override
    public void parseResponse(ComponentRequestDTO componentRequestDTO, FormViewResponseDTO formViewResponseDTO, int sortIndex) {

        parseComponentParser(componentRequestDTO, formViewResponseDTO, sortIndex);
    }

    public void parseComponentParser(ComponentRequestDTO componentRequestDTO, FormViewResponseDTO formViewResponseDTO, int sortIndex){
        //override by component parser and implementation will be there according to component
    }
}
