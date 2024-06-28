package com.examsofbharat.bramhsastra.akash.processors;

import com.examsofbharat.bramhsastra.akash.constants.AkashConstants;
import com.examsofbharat.bramhsastra.akash.factory.componentParser.BaseContentParser;
import com.examsofbharat.bramhsastra.akash.utils.DateUtils;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.jal.dto.*;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.examsofbharat.bramhsastra.akash.constants.AkashConstants.*;

@Component
@Slf4j
public class TimeAndFeeSummaryParser extends BaseContentParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void parseComponentParser(ComponentRequestDTO componentRequestDTO, FormViewResponseDTO formViewResponseDTO, int sortIndex) {
        try {
            AppTimeLineAndFeeDTO appTimeLineAndFeeDTO = new AppTimeLineAndFeeDTO();

            appTimeLineAndFeeDTO.setHeading("Important dates and Fee Details");
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

        AgeRelaxationDTO ageRelaxationDTO =  mapper.convertValue(
                enrichedFormDetailsDTO.getApplicationAgeDetailsDTO(), AgeRelaxationDTO.class);

        int minAge = enrichedFormDetailsDTO.getApplicationFormDTO().getMinAge();
        ageRelaxationDTO.setTitle(AkashConstants.AGE_RELAXATION_TITLE);
        ageRelaxationDTO.setCardColor(FormUtil.fetchCardColor(0));
        addYearSymbolInAge(ageRelaxationDTO, minAge);
        ageRelaxationDTO.setInformation("Above age is based on relaxation");

        return ageRelaxationDTO;

    }

    private ImportantDatesDTO buildsFormDates(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        ImportantDatesDTO importantDatesDTO = new ImportantDatesDTO();
        importantDatesDTO.setStartDate(DateUtils.getFormatedDate1(enrichedFormDetailsDTO.getApplicationFormDTO().getDateCreated()));
        importantDatesDTO.setExamDate("AS SCHEDULED");
        importantDatesDTO.setExamDateColor(GREEN_COLOR);
        importantDatesDTO.setLastDate(DateUtils.getFormatedDate1(enrichedFormDetailsDTO.getApplicationFormDTO().getEndDate()));
        importantDatesDTO.setLastDateColor(FormUtil.getLastXDaysDateColor(enrichedFormDetailsDTO.getApplicationFormDTO().getEndDate()));
        importantDatesDTO.setTitle(AkashConstants.DATES_TITLE);
        importantDatesDTO.setLastPayDate(DateUtils.getFormatedDate1(enrichedFormDetailsDTO.getApplicationFeeDTO().getLastPaymentDate()));
        importantDatesDTO.setLastPayDateColor(FormUtil.getLastXDaysDateColor(enrichedFormDetailsDTO.getApplicationFeeDTO().getLastPaymentDate()));
        importantDatesDTO.setCardColor(FormUtil.fetchCardColor(1));

        return importantDatesDTO;
    }

    private AppFeeDetailsDTO buildFeeDetails(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        AppFeeDetailsDTO appFeeDetailsDTO = mapper.convertValue(enrichedFormDetailsDTO.getApplicationFeeDTO(), AppFeeDetailsDTO.class);
        appFeeDetailsDTO.setTitle(AkashConstants.FEE_TITLE);
        addRupeeSymbolInFee(appFeeDetailsDTO);
        appFeeDetailsDTO.setCardColor(FormUtil.fetchCardColor(2));
        return appFeeDetailsDTO;
    }

    private void addRupeeSymbolInFee(AppFeeDetailsDTO appFeeDetailsDTO) {
        appFeeDetailsDTO.setSc(appFeeDetailsDTO.getSc() != null && feeChecker(appFeeDetailsDTO.getSc())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getSc() : null);
        appFeeDetailsDTO.setSt(appFeeDetailsDTO.getSt() != null && feeChecker(appFeeDetailsDTO.getSt())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getSt() : null);
        appFeeDetailsDTO.setFemale(appFeeDetailsDTO.getFemale() != null && feeChecker(appFeeDetailsDTO.getFemale())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getFemale() : null);
        appFeeDetailsDTO.setGeneral(appFeeDetailsDTO.getGeneral() != null && feeChecker(appFeeDetailsDTO.getGeneral())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getGeneral() : null);
        appFeeDetailsDTO.setObc(appFeeDetailsDTO.getObc() != null && feeChecker(appFeeDetailsDTO.getObc())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getObc() : null);
        appFeeDetailsDTO.setExArmy(appFeeDetailsDTO.getExArmy() != null && feeChecker(appFeeDetailsDTO.getExArmy())
                ? RUPEE_SYMBOL + appFeeDetailsDTO.getExArmy() : null);
    }

    private void addYearSymbolInAge(AgeRelaxationDTO ageRelaxationDTO, int minAge) {
        ageRelaxationDTO.setScAge(ageRelaxationDTO.getScAge() != null ? minAge + Integer.parseInt(ageRelaxationDTO.getScAge()) + YEAR_SYMBOL : null);
        ageRelaxationDTO.setStAge(ageRelaxationDTO.getStAge() != null ? minAge + Integer.parseInt(ageRelaxationDTO.getStAge()) + YEAR_SYMBOL : null);
        ageRelaxationDTO.setFemaleAge(ageRelaxationDTO.getFemaleAge() != null ? minAge + Integer.parseInt(ageRelaxationDTO.getFemaleAge()) + YEAR_SYMBOL : null);
        ageRelaxationDTO.setGeneralAge(ageRelaxationDTO.getGeneralAge() != null ? minAge + Integer.parseInt(ageRelaxationDTO.getGeneralAge()) + YEAR_SYMBOL : null);
        ageRelaxationDTO.setObcAge(ageRelaxationDTO.getObcAge() != null ? minAge + Integer.parseInt(ageRelaxationDTO.getObcAge()) + YEAR_SYMBOL : null);
        ageRelaxationDTO.setExArmy(ageRelaxationDTO.getExArmy() != null ? minAge + Integer.parseInt(ageRelaxationDTO.getExArmy()) + YEAR_SYMBOL : null);
    }

    public AgeAndFeeInformation buildInformation(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        AgeAndFeeInformation ageAndFeeInformation = new AgeAndFeeInformation();

        ageAndFeeInformation.setFeeInfo(enrichedFormDetailsDTO.getApplicationFeeDTO().getInformation());
        ageAndFeeInformation.setAgeInfo(enrichedFormDetailsDTO.getApplicationAgeDetailsDTO().getInformation());

        return ageAndFeeInformation;
    }

    public boolean feeChecker(String fee){
        fee = fee.trim();
        return !fee.equalsIgnoreCase("-1");
    }
}
