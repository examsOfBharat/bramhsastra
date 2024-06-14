package com.examsofbharat.bramhsastra.akash.processors;

import com.examsofbharat.bramhsastra.akash.factory.componentParser.BaseContentParser;
import com.examsofbharat.bramhsastra.akash.utils.DateUtils;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationFormIntroDTO;
import com.examsofbharat.bramhsastra.jal.dto.FormViewResponseDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ApplicationIntroParser extends BaseContentParser {

    @Override
    public void parseComponentParser(ComponentRequestDTO componentRequestDTO, FormViewResponseDTO formViewResponseDTO, int sortIndex){
        try {
            EnrichedFormDetailsDTO enrichedFormDetailsDTO = componentRequestDTO.getEnrichedFormDetailsDTO();

            ApplicationFormIntroDTO applicationFormIntroDTO = new ApplicationFormIntroDTO();
            applicationFormIntroDTO.setLastDate(DateUtils.getFormatedDate1(
                    enrichedFormDetailsDTO.getApplicationFormDTO().getEndDate()));
            applicationFormIntroDTO.setLastDateColor(FormUtil.getLastXDaysDateColor(
                    enrichedFormDetailsDTO.getApplicationFormDTO().getEndDate()));
            applicationFormIntroDTO.setTitle(componentRequestDTO.getApplicationNameDTO().getAppName());
            applicationFormIntroDTO.setType(enrichedFormDetailsDTO.getApplicationFormDTO().getState());
            applicationFormIntroDTO.setMinQualification(enrichedFormDetailsDTO.getApplicationFormDTO().getMinQualification());
            applicationFormIntroDTO.setLogoUrl(FormUtil.getLogoByName(componentRequestDTO.getApplicationNameDTO().getAppName()));

            long daysCount = DateUtils.getNoOfDaysFromToday(enrichedFormDetailsDTO.getApplicationFormDTO().getDateCreated());
            List<String> postedList = FormUtil.getPostedDetail(daysCount);

            applicationFormIntroDTO.setPostedOn(postedList.get(0));
            applicationFormIntroDTO.setPostedOnColor(postedList.get(1));

            applicationFormIntroDTO.setSubtitle("Government of Bharat");

            applicationFormIntroDTO.setVacancy(enrichedFormDetailsDTO.getApplicationFormDTO().getTotalVacancy());

            formViewResponseDTO.setApplicationFormIntroDTO(applicationFormIntroDTO);

        }catch (Exception e){
            log.error("Exception occurred while parsing formIntro formName::{}",
                    componentRequestDTO.getApplicationNameDTO().getAppName());
        }
    }
}
