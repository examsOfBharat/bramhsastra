package com.examsofbharat.bramhsastra.akash.processors.formProcessor;

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
import java.util.Objects;

@Slf4j
@Component
public class ApplicationIntroParser extends BaseContentParser {

    @Override
    public void parseComponentParser(ComponentRequestDTO componentRequestDTO, FormViewResponseDTO formViewResponseDTO, int sortIndex){
        try {
            EnrichedFormDetailsDTO enrichedFormDetailsDTO = componentRequestDTO.getEnrichedFormDetailsDTO();

            ApplicationFormIntroDTO applicationFormIntroDTO = new ApplicationFormIntroDTO();
            applicationFormIntroDTO.setAppId(enrichedFormDetailsDTO.getApplicationFormDTO().getId());

            if (Objects.nonNull(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO()) &&
                    enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getMinAge() > 0 &&
                    enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getMaxAge() > 0) {
                applicationFormIntroDTO.setAgeRange(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getMinAge() +
                        "-" + enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getMaxAge() + " years");
            }

            if(Objects.nonNull(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO()) &&
                    Objects.isNull(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getMaxNormalDob()) &&
                    Objects.isNull(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getMinNormalDob())){
                applicationFormIntroDTO.setCheckEligibility(false);
            }else{
                applicationFormIntroDTO.setCheckEligibility(true);
            }

            applicationFormIntroDTO.setLastDate(DateUtils.getFormatedDate1(
                    enrichedFormDetailsDTO.getApplicationFormDTO().getEndDate()));
            applicationFormIntroDTO.setLastDateColor(FormUtil.getExpiryDateColor(
                    enrichedFormDetailsDTO.getApplicationFormDTO().getEndDate()));
            applicationFormIntroDTO.setTitle(componentRequestDTO.getEnrichedFormDetailsDTO().
                    getApplicationFormDTO().getExamName());

            applicationFormIntroDTO.setType(enrichedFormDetailsDTO.getApplicationFormDTO().getState());
            applicationFormIntroDTO.setMinQualification(FormUtil.qualificationName.get(enrichedFormDetailsDTO.getApplicationFormDTO().getMinQualification()));
            applicationFormIntroDTO.setLogoUrl(FormUtil.getLogoByName(componentRequestDTO.getEnrichedFormDetailsDTO().
                    getApplicationFormDTO().getExamName()));
            applicationFormIntroDTO.setReleaseDate(DateUtils.getFormatedDate1(
                    enrichedFormDetailsDTO.getApplicationFormDTO().getStartDate()));

            long daysCount = DateUtils.getNoOfDaysFromToday(enrichedFormDetailsDTO.getApplicationFormDTO().getDateCreated());
            List<String> postedList = FormUtil.getPostedDetail(daysCount);

            applicationFormIntroDTO.setPostedOn(postedList.get(0));
            applicationFormIntroDTO.setPostedOnColor(postedList.get(1));

            if(enrichedFormDetailsDTO.getApplicationFormDTO().getState().equalsIgnoreCase("CENTRAL")) {
                applicationFormIntroDTO.setSubtitle("Government of Bharat");
            }else{
                applicationFormIntroDTO.setSubtitle("Government of " + enrichedFormDetailsDTO.getApplicationFormDTO().getState());
            }

            applicationFormIntroDTO.setVacancy(enrichedFormDetailsDTO.getApplicationFormDTO().getTotalVacancy());
            applicationFormIntroDTO.setApplyUrl(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getApply());
            applicationFormIntroDTO.setRegisterUrl(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getRegister());

            formViewResponseDTO.setApplicationFormIntroDTO(applicationFormIntroDTO);

        }catch (Exception e){
            log.error("Exception occurred while parsing formIntro formName::{}",
                    componentRequestDTO.getEnrichedFormDetailsDTO().
                            getApplicationFormDTO().getExamName());
        }
    }
}