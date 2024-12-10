package com.examsofbharat.bramhsastra.akash.processors.formProcessor;

import com.examsofbharat.bramhsastra.akash.factory.componentParser.BaseContentParser;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationFormDTO;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationUrlsDTO;
import com.examsofbharat.bramhsastra.jal.dto.FormViewResponseDTO;
import com.examsofbharat.bramhsastra.jal.dto.ImportantButtonDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.UrlManagerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ImportantButtonDetailParser extends BaseContentParser {

    @Override
    public void parseComponentParser(ComponentRequestDTO componentRequestDTO, FormViewResponseDTO formViewResponseDTO, int sortIndex) {
        try {
            ImportantButtonDetailsDTO importantButtonDetailsDTO = new ImportantButtonDetailsDTO();

            ApplicationFormDTO applicationFormDTO = null;
            if(Objects.nonNull(componentRequestDTO) &&
                    Objects.nonNull(componentRequestDTO.getEnrichedFormDetailsDTO())
            && Objects.nonNull(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationFormDTO())) {
                applicationFormDTO = componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationFormDTO();
            }

            ApplicationUrlsDTO applicationUrlsDTO = null;
            if(Objects.nonNull(componentRequestDTO) &&
                    Objects.nonNull(componentRequestDTO.getEnrichedFormDetailsDTO())
                    && Objects.nonNull(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO())) {
                applicationUrlsDTO = componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO();
            }

            //PYQ LIST
            if(!CollectionUtils.isEmpty(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().
                    getUrlList())){
                importantButtonDetailsDTO.setPyqList(componentRequestDTO
                        .getEnrichedFormDetailsDTO()
                        .getApplicationUrlsDTO().
                        getUrlList());
            }

            //Important Url List
            List<UrlManagerDTO> impUrlList = new ArrayList<>();
            // Always add the official website URL
            FormUtil.addUrlToList(impUrlList, "Official Website",
                    componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getOfficialWebsite());

            // Conditionally add URLs
            if (Objects.nonNull(applicationUrlsDTO)) {
                FormUtil.addUrlToList(impUrlList, "Notification", applicationUrlsDTO.getNotification());
                FormUtil.addUrlToList(impUrlList, "Syllabus", applicationUrlsDTO.getSyllabus());
                FormUtil.addUrlToList(impUrlList, "Ans-Key", applicationUrlsDTO.getAnsKey());
            }

            if (Objects.nonNull(applicationFormDTO)) {
                FormUtil.addUrlToList(impUrlList, "Admit-card", applicationFormDTO.getAdmitId());
                FormUtil.addUrlToList(impUrlList, "Result", applicationFormDTO.getResultId());
            }

            importantButtonDetailsDTO.setImpUrlList(impUrlList);
            formViewResponseDTO.setImportantButtonDetailsDTO(importantButtonDetailsDTO);

        }catch (Exception e){
            log.error("Exception occurred while parsing Time&Fee ",e);
        }
    }
}
