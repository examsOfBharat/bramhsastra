package com.examsofbharat.bramhsastra.akash.processors.formProcessor;

import com.examsofbharat.bramhsastra.akash.factory.componentParser.BaseContentParser;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationFormDTO;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationUrlsDTO;
import com.examsofbharat.bramhsastra.jal.dto.FormViewResponseDTO;
import com.examsofbharat.bramhsastra.jal.dto.ImportantButtonDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.UrlManagerDTO;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationUrl;
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

            //PYQ LIST
            if(!CollectionUtils.isEmpty(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().
                    getUrlList())){

                importantButtonDetailsDTO.setPyqList(componentRequestDTO
                        .getEnrichedFormDetailsDTO()
                        .getApplicationUrlsDTO().
                        getUrlList());
            }

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

            //Important Url List
            List<UrlManagerDTO> impUrlList = new ArrayList<>();
            UrlManagerDTO impUrlManagerDTO = new UrlManagerDTO();
            impUrlManagerDTO.setKey("Official Website");
            impUrlManagerDTO.setValue(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getOfficialWebsite());
            impUrlList.add(impUrlManagerDTO);


            if(Objects.nonNull(applicationUrlsDTO) && Objects.nonNull(applicationUrlsDTO.getNotification())) {
                UrlManagerDTO impUrlManagerDTO1 = new UrlManagerDTO();
                impUrlManagerDTO1.setKey("Notification");
                impUrlManagerDTO1.setValue(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getNotification());
                impUrlList.add(impUrlManagerDTO1);
            }


            if(Objects.nonNull(applicationUrlsDTO) && Objects.nonNull(applicationUrlsDTO.getSyllabus())) {
                UrlManagerDTO impUrlManagerDTO2 = new UrlManagerDTO();
                impUrlManagerDTO2.setKey("Syllabus");
                impUrlManagerDTO2.setValue(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getSyllabus());
                impUrlList.add(impUrlManagerDTO2);
            }


            if(Objects.nonNull(applicationUrlsDTO) && Objects.nonNull(applicationUrlsDTO.getAnsKey())) {
                UrlManagerDTO impUrlManagerDTO3 = new UrlManagerDTO();
                impUrlManagerDTO3.setKey("Ans-Key");
                impUrlManagerDTO3.setValue(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getAnsKey());
                impUrlList.add(impUrlManagerDTO3);
            }


            if(Objects.nonNull(applicationFormDTO) && StringUtil.notEmpty(applicationFormDTO.getAdmitId())) {
                UrlManagerDTO impUrlManagerDTO4 = new UrlManagerDTO();
                impUrlManagerDTO4.setKey("Admit-card");
                impUrlManagerDTO4.setValue(applicationFormDTO.getAdmitId());
                impUrlList.add(impUrlManagerDTO4);
            }

            if(Objects.nonNull(applicationFormDTO) && StringUtil.notEmpty(applicationFormDTO.getResultId())) {
                UrlManagerDTO impUrlManagerDTO5 = new UrlManagerDTO();
                impUrlManagerDTO5.setKey("Result");
                impUrlManagerDTO5.setValue(applicationFormDTO.getResultId());
                impUrlList.add(impUrlManagerDTO5);
            }

            importantButtonDetailsDTO.setImpUrlList(impUrlList);
            formViewResponseDTO.setImportantButtonDetailsDTO(importantButtonDetailsDTO);

        }catch (Exception e){
            log.error("Exception occurred while parsing Time&Fee ",e);
        }
    }
}
