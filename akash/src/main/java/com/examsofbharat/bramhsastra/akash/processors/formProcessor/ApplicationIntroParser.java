package com.examsofbharat.bramhsastra.akash.processors.formProcessor;

import com.examsofbharat.bramhsastra.akash.factory.componentParser.BaseContentParser;
import com.examsofbharat.bramhsastra.akash.utils.DateUtils;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationFormDTO;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationFormIntroDTO;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationSeoDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.FormViewResponseDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
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
                        " - " + enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getMaxAge() + " years");
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

            //main form logo
            applicationFormIntroDTO.setLogoUrl(FormUtil.getLogoByName(componentRequestDTO.getEnrichedFormDetailsDTO().
                    getApplicationFormDTO().getExamName()));

            //logo for sharing url
            applicationFormIntroDTO.setShareLogoUrl(FormUtil.getPngLogoByName(componentRequestDTO.getEnrichedFormDetailsDTO().
                    getApplicationFormDTO().getExamName()));

            //Setting release date
            if(enrichedFormDetailsDTO.getApplicationFormDTO().getStartDate().compareTo(DateUtils.getStartOfDay(new Date())) >= 0){
                applicationFormIntroDTO.setReleaseDateTitle("Application Start Date : ");
            }else{
                applicationFormIntroDTO.setReleaseDateTitle("Application Start Date : ");
            }
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

            if(enrichedFormDetailsDTO.getApplicationFormDTO().getTotalVacancy() > 0) {
                applicationFormIntroDTO.setVacancy(FormUtil.formatIntoIndianNumSystem(enrichedFormDetailsDTO.getApplicationFormDTO().getTotalVacancy()));
            }else {
                applicationFormIntroDTO.setVacancy("Not Available");
            }
            applicationFormIntroDTO.setApplyUrl(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getApply());
            applicationFormIntroDTO.setRegisterUrl(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getRegister());

            if(StringUtil.notEmpty(enrichedFormDetailsDTO.getApplicationFormDTO().getQualification())){
                applicationFormIntroDTO.setQualificationKey("Qualification");
                applicationFormIntroDTO.setQualificationValue(enrichedFormDetailsDTO.getApplicationFormDTO()
                        .getQualification());
            }

            //form status banner condition
            ApplicationFormDTO applicationFormDTO = enrichedFormDetailsDTO.getApplicationFormDTO();
            if(applicationFormDTO.getDateModified().compareTo(DateUtils.addDays(applicationFormDTO.getDateCreated(), 5)) > 0){
                applicationFormIntroDTO.setFormStatus("UPDATES");
            }else if(new Date().after(applicationFormDTO.getEndDate())){
                applicationFormIntroDTO.setFormStatus("EXPIRED");
            }else if(FormUtil.dateIsWithinXDays(applicationFormDTO.getStartDate())){
                applicationFormIntroDTO.setFormStatus("NEW");
            }

            //populate seo details
            ApplicationSeoDetailsDTO seoDetailsDTO = enrichedFormDetailsDTO.getApplicationSeoDetailsDTO();
            if(Objects.nonNull(seoDetailsDTO)){
                applicationFormIntroDTO.setSeoTitle(seoDetailsDTO.getTitle());
                applicationFormIntroDTO.setSeoKeywords(seoDetailsDTO.getKeywords());
                applicationFormIntroDTO.setSeoDescription(seoDetailsDTO.getDescription());
            }

            formViewResponseDTO.setApplicationFormIntroDTO(applicationFormIntroDTO);

        }catch (Exception e){
            log.error("Exception occurred while parsing formIntro formName::{}",
                    componentRequestDTO.getEnrichedFormDetailsDTO().
                            getApplicationFormDTO().getExamName());
        }
    }
}
