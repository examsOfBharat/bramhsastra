package com.examsofbharat.bramhsastra.akash.processors.formProcessor;

import com.examsofbharat.bramhsastra.akash.factory.componentParser.BaseContentParser;
import com.examsofbharat.bramhsastra.jal.dto.FormViewResponseDTO;
import com.examsofbharat.bramhsastra.jal.dto.ImportantButtonDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ImportantButtonDetailParser extends BaseContentParser {

    @Override
    public void parseComponentParser(ComponentRequestDTO componentRequestDTO, FormViewResponseDTO formViewResponseDTO, int sortIndex) {
        try {
            ImportantButtonDetailsDTO importantButtonDetailsDTO = new ImportantButtonDetailsDTO();

            importantButtonDetailsDTO.setAdmitCardId(
                    StringUtil.notEmpty(
                                    componentRequestDTO.
                                    getEnrichedFormDetailsDTO().
                                    getApplicationFormDTO().
                                    getAdmitId())
                            ? componentRequestDTO.
                            getEnrichedFormDetailsDTO().
                            getApplicationFormDTO().
                            getAdmitId() : null
            );

            importantButtonDetailsDTO.setResultId(
                    StringUtil.notEmpty(
                                    componentRequestDTO.
                                    getEnrichedFormDetailsDTO().
                                    getApplicationFormDTO().
                                    getResultId())
                            ? componentRequestDTO.
                            getEnrichedFormDetailsDTO().
                            getApplicationFormDTO().
                            getResultId() : null
            );

            importantButtonDetailsDTO.setNotificationUrl(
                            componentRequestDTO.
                            getEnrichedFormDetailsDTO().
                            getApplicationUrlsDTO().
                            getNotification()
            );

            importantButtonDetailsDTO.setSyllabusUrl(
                    componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getSyllabus());

            importantButtonDetailsDTO.setAnsKeyUrl(
                    componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getAnsKey());
            importantButtonDetailsDTO.setOfficialWebsite(
                    componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getOfficialWebsite()
            );

            formViewResponseDTO.setImportantButtonDetailsDTO(importantButtonDetailsDTO);

        }catch (Exception e){
            log.error("Exception occurred while parsing Time&Fee ",e);
        }
    }
}