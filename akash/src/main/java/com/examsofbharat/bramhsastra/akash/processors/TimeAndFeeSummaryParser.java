package com.examsofbharat.bramhsastra.akash.processors;

import com.examsofbharat.bramhsastra.akash.constants.AkashConstants;
import com.examsofbharat.bramhsastra.akash.factory.componentParser.BaseContentParser;
import com.examsofbharat.bramhsastra.akash.utils.DateUtils;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.jal.dto.*;
import com.examsofbharat.bramhsastra.jal.dto.request.ComponentRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.examsofbharat.bramhsastra.akash.constants.AkashConstants.GREEN_COLOR;
import static com.examsofbharat.bramhsastra.akash.constants.AkashConstants.RUPEE_SYMBOL;

@Component
public class TimeAndFeeSummaryParser extends BaseContentParser {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(TimeAndFeeSummaryParser.class);

    @Override
    public void parseComponentParser(ComponentRequestDTO componentRequestDTO, FormViewResponseDTO formViewResponseDTO, int sortIndex) {
        try {
            AppTimeLineAndFeeDTO appTimeLineAndFeeDTO = new AppTimeLineAndFeeDTO();

            appTimeLineAndFeeDTO.setHeading("Important dates and Fee Details");
            appTimeLineAndFeeDTO.setAgeRelaxationDTO(buildAgeRelaxation(componentRequestDTO.getEnrichedFormDetailsDTO()));
            appTimeLineAndFeeDTO.setImportantDatesDto(buildsFormDates(componentRequestDTO.getEnrichedFormDetailsDTO()));
            appTimeLineAndFeeDTO.setAppFeeDetailsDto(buildFeeDetails(componentRequestDTO.getEnrichedFormDetailsDTO()));

            formViewResponseDTO.setAppTimeLineAndFeeDTO(appTimeLineAndFeeDTO);
        }catch (Exception e){
            log.error("Exception occurred while parsing Time&Fee ",e);
        }
    }

    private AgeRelaxationDTO buildAgeRelaxation(EnrichedFormDetailsDTO enrichedFormDetailsDTO){

        AgeRelaxationDTO ageRelaxationDTO =  mapper.convertValue(
                enrichedFormDetailsDTO.getApplicationAgeDetailsDTO(), AgeRelaxationDTO.class);
        ageRelaxationDTO.setTitle(AkashConstants.AGE_RELAXATION_TITLE);
        ageRelaxationDTO.setCardColor(FormUtil.fetchCardColor(0));

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
        appFeeDetailsDTO.setSc(appFeeDetailsDTO.getSc() != null ? RUPEE_SYMBOL + appFeeDetailsDTO.getSc() : null);
        appFeeDetailsDTO.setSt(appFeeDetailsDTO.getSt() != null ? RUPEE_SYMBOL + appFeeDetailsDTO.getSt() : null);
        appFeeDetailsDTO.setFemale(appFeeDetailsDTO.getFemale() != null ? RUPEE_SYMBOL + appFeeDetailsDTO.getFemale() : null);
        appFeeDetailsDTO.setGeneral(appFeeDetailsDTO.getGeneral() != null ? RUPEE_SYMBOL + appFeeDetailsDTO.getGeneral() : null);
        appFeeDetailsDTO.setObc(appFeeDetailsDTO.getObc() != null ? RUPEE_SYMBOL + appFeeDetailsDTO.getObc() : null);
        appFeeDetailsDTO.setExArmy(appFeeDetailsDTO.getExArmy() != null ? RUPEE_SYMBOL + appFeeDetailsDTO.getExArmy() : null);
    }
}
