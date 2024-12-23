package com.examsofbharat.bramhsastra.akash.processors.formProcessor;

import com.examsofbharat.bramhsastra.akash.constants.AkashConstants;
import com.examsofbharat.bramhsastra.akash.factory.componentParser.BaseContentParser;
import com.examsofbharat.bramhsastra.akash.utils.DateUtils;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.jal.dto.*;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.examsofbharat.bramhsastra.akash.constants.AkashConstants.*;

@Component
@Slf4j
public class TimeAndFeeSummaryParser extends BaseContentParser {

    final String subT = "Important dates and Fee";

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void parseComponentParser(ComponentRequestDTO componentRequestDTO, FormViewResponseDTO formViewResponseDTO, int sortIndex) {
        try {
            AppTimeLineAndFeeDTO appTimeLineAndFeeDTO = new AppTimeLineAndFeeDTO();

            String subTitle = StringUtil.notEmpty(componentRequestDTO.
                    getEnrichedFormDetailsDTO().
                    getApplicationAgeDetailsDTO().
                    getSubTitle()) ? componentRequestDTO.
                    getEnrichedFormDetailsDTO().
                    getApplicationAgeDetailsDTO().
                    getSubTitle() + subT : subT;

            appTimeLineAndFeeDTO.setHeading(subTitle);
            appTimeLineAndFeeDTO.setAgeRelaxationDTO(buildAgeRelaxation(componentRequestDTO.getEnrichedFormDetailsDTO()));
            appTimeLineAndFeeDTO.setImportantDatesDto(buildsFormDates(componentRequestDTO.getEnrichedFormDetailsDTO()));
            appTimeLineAndFeeDTO.setAppFeeDetailsDto(buildFeeDetails(componentRequestDTO.getEnrichedFormDetailsDTO()));
            appTimeLineAndFeeDTO.setAgeAndFeeInformation(buildInformation(componentRequestDTO.getEnrichedFormDetailsDTO()));

            formViewResponseDTO.setAppTimeLineAndFeeDTO(appTimeLineAndFeeDTO);
        }catch (Exception e){
            log.error("Exception occurred while parsing Time&Fee ",e);
        }
    }

    private AgeRelaxationDTO buildAgeRelaxation(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        //if max-age is not available then remove the whole card (this cases can be seen in case of mains exams)
        if(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO() == null || enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getMaxAge() <=0){
            return null;
        }

        AgeRelaxationDTO ageRelaxationDTO =  mapper.convertValue(
                enrichedFormDetailsDTO.getApplicationAgeDetailsDTO(), AgeRelaxationDTO.class);

        int maxAge = enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getMaxAge();
        ageRelaxationDTO.setTitle(AkashConstants.AGE_RELAXATION_TITLE);

        ageRelaxationDTO.setCardColor(FormUtil.fetchCardColor(0));
        ageRelaxationDTO.setUpdates(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getUpdates());
        ageRelaxationDTO.setUpdatesDate(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getDateModified());
        addYearSymbolInAge(ageRelaxationDTO, maxAge);
        ageRelaxationDTO.setInformation("Above age is based on relaxation");

        return ageRelaxationDTO;

    }

    private ImportantDatesDTO buildsFormDates(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        ImportantDatesDTO importantDatesDTO = new ImportantDatesDTO();
        List<DateDetailsDTO> dateDetailsDTOList = new ArrayList<>();

        importantDatesDTO.setTitle(AkashConstants.DATES_TITLE);
        importantDatesDTO.setCardColor(FormUtil.fetchCardColor(1));

        populateDateDetails(dateDetailsDTOList,"Start Date ", DateUtils.getFormatedDate1(enrichedFormDetailsDTO.getApplicationFormDTO().getStartDate()),
                FormUtil.getLastXDaysDateColor(enrichedFormDetailsDTO.getApplicationFormDTO().getStartDate()));

        populateDateDetails(dateDetailsDTOList, "Last Date ", DateUtils.getFormatedDate1(enrichedFormDetailsDTO.getApplicationFormDTO().getEndDate()),
                FormUtil.getLastXDaysDateColor(enrichedFormDetailsDTO.getApplicationFormDTO().getEndDate()));

        populateDateDetails(dateDetailsDTOList, "Last Pay Date ", DateUtils.getFormatedDate1(enrichedFormDetailsDTO.getApplicationFeeDTO().getLastPaymentDate()),
                FormUtil.getLastXDaysDateColor(enrichedFormDetailsDTO.getApplicationFeeDTO().getLastPaymentDate()));

        if(StringUtil.notEmpty(enrichedFormDetailsDTO.getApplicationFeeDTO().getCorrectionDate())){
            populateDateDetails(dateDetailsDTOList, "Correction Date ", enrichedFormDetailsDTO.getApplicationFeeDTO().getCorrectionDate(),
                    GREEN_COLOR);
        }

        if(StringUtil.notEmpty(enrichedFormDetailsDTO.getApplicationFeeDTO().getExamDate())){
            populateDateDetails(dateDetailsDTOList, "Exam Date ", enrichedFormDetailsDTO.getApplicationFeeDTO().getExamDate(),
                    GREEN_COLOR);
        }

        buildExtraDate(dateDetailsDTOList,
                enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getExtraDateDetails());

        importantDatesDTO.setDateDetails(dateDetailsDTOList);

        return importantDatesDTO;
    }

    private void populateDateDetails(List<DateDetailsDTO> dateDetailsDTOList,
                                     String title, String date, String color){

        DateDetailsDTO dateDetailsDTO = new DateDetailsDTO();
        dateDetailsDTO.setTitle(title);
        dateDetailsDTO.setDate(date);
        dateDetailsDTO.setDateColor(color);

        dateDetailsDTOList.add(dateDetailsDTO);
    }

    private AppFeeDetailsDTO buildFeeDetails(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        if(Objects.isNull(enrichedFormDetailsDTO.getApplicationFeeDTO())) return null;

        AppFeeDetailsDTO appFeeDetailsDTO = mapper.convertValue(enrichedFormDetailsDTO.getApplicationFeeDTO(), AppFeeDetailsDTO.class);
        appFeeDetailsDTO.setTitle(AkashConstants.FEE_TITLE);
        addRupeeSymbolInFee(appFeeDetailsDTO);
        appFeeDetailsDTO.setCardColor(FormUtil.fetchCardColor(2));
        return appFeeDetailsDTO;
    }

    private void addRupeeSymbolInFee(AppFeeDetailsDTO appFeeDetailsDTO) {
        appFeeDetailsDTO.setSc(isValidValue(appFeeDetailsDTO.getSc())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getSc() : null);
        appFeeDetailsDTO.setPwd(isValidValue(appFeeDetailsDTO.getPwd())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getPwd() : null);
        appFeeDetailsDTO.setSt(isValidValue(appFeeDetailsDTO.getSt())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getSt() : null);
        appFeeDetailsDTO.setFemale(isValidValue(appFeeDetailsDTO.getFemale())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getFemale() : null);
        appFeeDetailsDTO.setGeneral(isValidValue(appFeeDetailsDTO.getGeneral())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getGeneral() : null);
        appFeeDetailsDTO.setObc(isValidValue(appFeeDetailsDTO.getObc())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getObc() : null);
        appFeeDetailsDTO.setExArmy(isValidValue(appFeeDetailsDTO.getExArmy())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getExArmy() : null);
    }

    private void addYearSymbolInAge(AgeRelaxationDTO ageRelaxationDTO, int maxAge) {
        ageRelaxationDTO.setScAge(isValidValue(ageRelaxationDTO.getScAge()) ? maxAge + Integer.parseInt(ageRelaxationDTO.getScAge()) + YEAR_SYMBOL : null);
        ageRelaxationDTO.setStAge(isValidValue(ageRelaxationDTO.getStAge()) ? maxAge + Integer.parseInt(ageRelaxationDTO.getStAge()) + YEAR_SYMBOL : null);
        ageRelaxationDTO.setFemaleAge(isValidValue(ageRelaxationDTO.getFemaleAge()) ? maxAge + Integer.parseInt(ageRelaxationDTO.getFemaleAge()) + YEAR_SYMBOL : null);
        ageRelaxationDTO.setGeneralAge(isValidValue(ageRelaxationDTO.getGeneralAge()) ? maxAge + Integer.parseInt(ageRelaxationDTO.getGeneralAge()) + YEAR_SYMBOL : null);
        ageRelaxationDTO.setObcAge(isValidValue(ageRelaxationDTO.getObcAge()) ? maxAge + Integer.parseInt(ageRelaxationDTO.getObcAge()) + YEAR_SYMBOL : null);
        ageRelaxationDTO.setExArmy(isValidValue(ageRelaxationDTO.getExArmy()) ? maxAge + Integer.parseInt(ageRelaxationDTO.getExArmy()) + YEAR_SYMBOL : null);
    }

    public AgeAndFeeInformation buildInformation(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        AgeAndFeeInformation ageAndFeeInformation = new AgeAndFeeInformation();

        if(Objects.nonNull(enrichedFormDetailsDTO.getApplicationFeeDTO())) {
            ageAndFeeInformation.setFeeInfo(enrichedFormDetailsDTO.getApplicationFeeDTO().getInformation());
        }
        if(Objects.nonNull(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO())) {
            ageAndFeeInformation.setAgeInfo(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getInformation());
        }

        return ageAndFeeInformation;
    }

    private void buildExtraDate(List<DateDetailsDTO> dateDetailsDTOS, String extraDates){

        if(StringUtil.isEmpty(extraDates))
            return;
        String[] titleDate = extraDates.split(",");
        for(String title : titleDate){
            String[] titleDat = title.trim().split(":");
            if(titleDat.length < 2){
                continue;
            }
            DateDetailsDTO dateDetailsDTO = new DateDetailsDTO();
            dateDetailsDTO.setTitle(titleDat[0]);
            dateDetailsDTO.setDate(titleDat[1]);
            dateDetailsDTO.setDateColor(GREEN_COLOR);

            dateDetailsDTOS.add(dateDetailsDTO);
        }
    }


    public boolean isValidValue(String fee){
        if(StringUtil.isEmpty(fee))
            return false;
        fee = fee.trim();
        if(fee.equalsIgnoreCase("-1") || fee.equalsIgnoreCase("-1.0"))
            return false;
        return true;
    }

    public static void main(String[] args) {
        String extraDate = "ADMIT CARD DATE:20 Aug 2024, RESULT DATE:20 Sep 2024";
        String[] titleDate = extraDate.split(",");
        for(String title : titleDate){
            String[] titleDat = title.trim().split(":");
            if(titleDat.length < 2){
                continue;
            }
            System.out.println(titleDat[0]);
            System.out.println(titleDat[1]);
        }
    }
}
