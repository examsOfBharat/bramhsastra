package com.examsofbharat.bramhsastra.akash.processors.formProcessor;

import com.examsofbharat.bramhsastra.akash.factory.componentParser.BaseContentParser;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.jal.dto.*;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class VacancyEligibilitySummaryParser extends BaseContentParser {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(VacancyEligibilitySummaryParser.class);

    @Override
    public void parseComponentParser(ComponentRequestDTO componentRequestDTO, FormViewResponseDTO formViewResponseDTO, int sortIndex) {
        try {
            VacancyEligibilityCardDetailsDTO vacancyEligibilityCardDetailsDTO = new VacancyEligibilityCardDetailsDTO();
            vacancyEligibilityCardDetailsDTO.setTitle("Vacancy and Eligibility details");
            vacancyEligibilityCardDetailsDTO.setSortIndex(sortIndex);
            buildEligibilityData(vacancyEligibilityCardDetailsDTO, componentRequestDTO.getEnrichedFormDetailsDTO());

            formViewResponseDTO.setVacancyEligibilityCardDetailsDTO(vacancyEligibilityCardDetailsDTO);
        }catch (Exception e){
            log.error("Exception occurred while parsing vacancy&Eligibility data ",e);
        }

    }

    public void buildEligibilityData(VacancyEligibilityCardDetailsDTO vacancyEligibilityData,
                                     EnrichedFormDetailsDTO enrichedFormDetailsDTO){

        List<VacancyEligibilityDataDTO> vedList = new ArrayList<>();

        if(CollectionUtils.isEmpty(enrichedFormDetailsDTO.getApplicationVacancyDTOS())
                && CollectionUtils.isEmpty(enrichedFormDetailsDTO.getApplicationEligibilityDTOS())){
            return;
        }

        if(enrichedFormDetailsDTO.getApplicationVacancyDTOS().size() !=
                enrichedFormDetailsDTO.getApplicationEligibilityDTOS().size()){

            vedList.add(buildVEData(enrichedFormDetailsDTO.getApplicationVacancyDTOS().get(0),
                    enrichedFormDetailsDTO.getApplicationEligibilityDTOS().get(0)));
            return;
        }

        List<ApplicationEligibilityDTO> eligibilityDataDTOS = enrichedFormDetailsDTO.getApplicationEligibilityDTOS();
        //sort based on sequence
        FormUtil.eligibilityComparator(eligibilityDataDTOS);

        List<ApplicationVacancyDTO> vacancyDTOS = enrichedFormDetailsDTO.getApplicationVacancyDTOS();
        //sort based on sequence
        FormUtil.vacancyComparator(vacancyDTOS);


        for(int i = 0; i < enrichedFormDetailsDTO.getApplicationVacancyDTOS().size(); i++){

            VacancyEligibilityDataDTO vacancyEligibilityDataDTO = buildVEData(enrichedFormDetailsDTO.getApplicationVacancyDTOS().get(i),
                    enrichedFormDetailsDTO.getApplicationEligibilityDTOS().get(i));
            vacancyEligibilityDataDTO.setCardColor(FormUtil.fetchCardColor(i%4));
            vedList.add(vacancyEligibilityDataDTO);
        }

        vacancyEligibilityData.setVacancyEligibilityDataList(vedList);
    }

    public VacancyEligibilityDataDTO buildVEData(ApplicationVacancyDTO applicationVacancyDTO,
                                                 ApplicationEligibilityDTO applicationEligibilityDTO){

        VacancyEligibilityDataDTO vacancyEligibilityDataDTO = new VacancyEligibilityDataDTO();

        vacancyEligibilityDataDTO.setEligibilityDataDTO(mapper.convertValue(applicationEligibilityDTO, EligibilityDataDTO.class));
        vacancyEligibilityDataDTO.setVacancyDetailsDTO(mapper.convertValue(applicationVacancyDTO, VacancyResDetailsDTO.class));
        vacancyEligibilityDataDTO.setDepartment(applicationVacancyDTO.getDepartment());

        return vacancyEligibilityDataDTO;
    }

}
