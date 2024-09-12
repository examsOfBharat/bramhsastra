package com.examsofbharat.bramhsastra.akash.processors.formProcessor;

import com.examsofbharat.bramhsastra.akash.factory.componentParser.BaseContentParser;
import com.examsofbharat.bramhsastra.akash.utils.DateUtils;
import com.examsofbharat.bramhsastra.akash.utils.EobInitilizer;
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

            ApplicationFormDTO applicationFormDTO = enrichedFormDetailsDTO.getApplicationFormDTO();

            ApplicationFormIntroDTO applicationFormIntroDTO = new ApplicationFormIntroDTO();
            applicationFormIntroDTO.setAppId(enrichedFormDetailsDTO.getApplicationFormDTO().getId());
            applicationFormIntroDTO.setCardColor(EobInitilizer.getThirdPageColor().get(
                    FormUtil.genRandomNo()
            ));

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
                    applicationFormDTO.getEndDate()));
            applicationFormIntroDTO.setLastDateColor(FormUtil.getExpiryDateColor(
                    applicationFormDTO.getEndDate()));
            applicationFormIntroDTO.setTitle(componentRequestDTO.getEnrichedFormDetailsDTO().
                    getApplicationFormDTO().getExamName());

            applicationFormIntroDTO.setType(applicationFormDTO.getState());
            applicationFormIntroDTO.setMinQualification(FormUtil.qualificationName.get(enrichedFormDetailsDTO.getApplicationFormDTO().getMinQualification()));

            //main form logo
            applicationFormIntroDTO.setLogoUrl(FormUtil.getLogoByName(applicationFormDTO.getExamName()));

            //logo for sharing url
            applicationFormIntroDTO.setShareLogoUrl(FormUtil.getPngLogoByName(applicationFormDTO.getExamName()));

            //Setting release date
            if(applicationFormDTO.getStartDate().compareTo(DateUtils.getStartOfDay(new Date())) >= 0){
                applicationFormIntroDTO.setReleaseDateTitle("Application Start Date : ");
            }else{
                applicationFormIntroDTO.setReleaseDateTitle("Application Start Date : ");
            }
            applicationFormIntroDTO.setReleaseDate(DateUtils.getFormatedDate1(
                    applicationFormDTO.getStartDate()));


            long daysCount = DateUtils.getNoOfDaysFromToday(applicationFormDTO.getDateCreated());
            List<String> postedList = FormUtil.getPostedDetail(daysCount);

            applicationFormIntroDTO.setPostedOn(postedList.get(0));
            applicationFormIntroDTO.setPostedOnColor(postedList.get(1));

            if(applicationFormDTO.getState().equalsIgnoreCase("CENTRAL")) {
                applicationFormIntroDTO.setSubtitle("Government of Bharat");
            }else{
                applicationFormIntroDTO.setSubtitle("Government of " + applicationFormDTO.getState());
            }

            if(applicationFormDTO.getTotalVacancy() > 0) {
                applicationFormIntroDTO.setVacancy(FormUtil.formatIntoIndianNumSystem(enrichedFormDetailsDTO.getApplicationFormDTO().getTotalVacancy()));
            }else {
                applicationFormIntroDTO.setVacancy("Not Available");
            }

            //remove apply and register button for expired form
            if(new Date().before(applicationFormDTO.getEndDate())) {
                applicationFormIntroDTO.setApplyUrl(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getApply());
                applicationFormIntroDTO.setRegisterUrl(componentRequestDTO.getEnrichedFormDetailsDTO().getApplicationUrlsDTO().getRegister());
            }

            if(StringUtil.notEmpty(applicationFormDTO.getQualification())){
                applicationFormIntroDTO.setQualificationKey("Qualification");
                applicationFormIntroDTO.setQualificationValue(applicationFormDTO
                        .getQualification());
            }

            //form status banner condition
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
