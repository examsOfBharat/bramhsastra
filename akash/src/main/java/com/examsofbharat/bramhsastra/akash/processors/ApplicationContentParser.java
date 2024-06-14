package com.examsofbharat.bramhsastra.akash.processors;

import com.examsofbharat.bramhsastra.akash.factory.componentParser.BaseContentParser;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationContentManagerDTO;
import com.examsofbharat.bramhsastra.jal.dto.FormViewResponseDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ApplicationContentParser extends BaseContentParser {

    @Override
    public void parseComponentParser(ComponentRequestDTO componentRequestDTO, FormViewResponseDTO formViewResponseDTO, int sortIndex){
        try {
            if (CollectionUtils.isEmpty(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationContentManagerDTO())) {
                return;
            }

            List<ApplicationContentManagerDTO> contentManagerList = new ArrayList<>(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationContentManagerDTO());

            formViewResponseDTO.setApplicationContentManagers(contentManagerList);
        }catch (Exception e){
            log.error("Exception occurred while parsing app content ",e);
        }
    }
}
