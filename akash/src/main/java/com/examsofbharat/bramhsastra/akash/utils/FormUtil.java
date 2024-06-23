package com.examsofbharat.bramhsastra.akash.utils;

import com.examsofbharat.bramhsastra.akash.constants.AkashConstants;
import com.examsofbharat.bramhsastra.jal.dto.response.EligibilityCheckResponseDTO;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationAgeDetails;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;

import static com.examsofbharat.bramhsastra.akash.constants.AkashConstants.*;
import static com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum.*;
import static com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum.ADMIT;
import static com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum.RESULT;
import static com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum.*;

@Slf4j
@Component
public class FormUtil {

    public final static Map<FormSubTypeEnum, FormTypeEnum> formSubTypeMap = new HashMap<>();
    public static final List<String> relatedFormType = new ArrayList<>();
    public static List<String> cardColor = new ArrayList<>();

    @PostConstruct
    public void init() {
        initFormTypeMap();
        initVacancyColor();
        initRelatedFormType();
    }

    public static String getLastXDaysDateColor(Date date){
        if(date == null) return null;
        boolean flag = DateUtils.isTimePassedDays(5, date);
        if(flag)
            return RED_COLOR;
        return GREEN_COLOR;
    }

    public static String getFormShowDateColor(Date showDate){
        boolean flag = DateUtils.isTimePassedDays(3, showDate);

        if(flag)
            return GREEN_COLOR;
        return BLUE_COLOR;
    }

    public static String formatExamDate(Date examDate){
        return DateUtils.getFormatedDate1(examDate);
    }

    public void initFormTypeMap(){
        formSubTypeMap.put(ADMIT, FormTypeEnum.ADMIT);
        formSubTypeMap.put(RESULT, FormTypeEnum.RESULT);

        formSubTypeMap.put(TENTH, QUALIFICATION_BASED);
        formSubTypeMap.put(TWELFTH, QUALIFICATION_BASED);
        formSubTypeMap.put(DIPLOMA, QUALIFICATION_BASED);
        formSubTypeMap.put(GRADUATE, QUALIFICATION_BASED);
        formSubTypeMap.put(ABOVE_GRADUATE, QUALIFICATION_BASED);


        formSubTypeMap.put(BANKING, SECTOR_BASED);
        formSubTypeMap.put(PSU_JOB, SECTOR_BASED);
        formSubTypeMap.put(AGRICULTURE, SECTOR_BASED);
        formSubTypeMap.put(DEFENSE_SERVICE, SECTOR_BASED);
        formSubTypeMap.put(STATE_POLICE, SECTOR_BASED);
        formSubTypeMap.put(CIVIL_SERVICES, SECTOR_BASED);
        formSubTypeMap.put(MANAGEMENT,SECTOR_BASED);
        formSubTypeMap.put(RAILWAY, SECTOR_BASED);
        formSubTypeMap.put(SSC_CENTRAL, SECTOR_BASED);
        formSubTypeMap.put(LAW, SECTOR_BASED);


        formSubTypeMap.put(A_GRADE, GRADE_BASED);
        formSubTypeMap.put(B_GRADE, GRADE_BASED);
        formSubTypeMap.put(C_GRADE, GRADE_BASED);
        formSubTypeMap.put(D_GRADE, GRADE_BASED);

        formSubTypeMap.put(CENTRAL, PROVINCIAL_BASED);

        log.info(formSubTypeMap.toString());
    }

    public void initVacancyColor(){
        cardColor.add("#fef9ec");
        cardColor.add("#e8f1fb");
        cardColor.add("#f4f2f7");
        cardColor.add("#e6f4ec");
    }

    public void initRelatedFormType(){
        relatedFormType.add(BANKING.name());
        relatedFormType.add(LAW.name());
        relatedFormType.add(SSC_CENTRAL.name());
        relatedFormType.add(CIVIL_SERVICES.name());
        relatedFormType.add(DEFENSE_SERVICE.name());
        relatedFormType.add(STATE_POLICE.name());
        relatedFormType.add(MANAGEMENT.name());
        relatedFormType.add(PSU_JOB.name());
        relatedFormType.add(AGRICULTURE.name());
        relatedFormType.add(MANAGEMENT.name());
        relatedFormType.add(RAILWAY.name());
    }

    public static FormTypeEnum getFormType(FormSubTypeEnum subType){
        return formSubTypeMap.get(subType);
    }

    public static List<String> getRelatedFormType(){ return relatedFormType; }

    public static List<String> getPostedDetail(long daysCount){
        List<String> postDataList = new ArrayList<>();
        if(daysCount == 0){
            postDataList.add("Today");
            postDataList.add(GREEN_COLOR);
        } else if (daysCount == 1) {
            postDataList.add("Yesterday");
            postDataList.add(GREEN_COLOR);
        }
        else{
            postDataList.add(daysCount + AkashConstants.X_DAYS);
            postDataList.add(RED_COLOR);
        }
        return postDataList;
    }

    public static String getLogoByName(String formName){
        String[] nameList = formName.split(" ");
        String url;
        for(String str : nameList){
            url = EobInitilizer.getLogoByName(str);
            if(Objects.nonNull(url)){
                return url;
            }
        }
        return EobInitilizer.getLogoByName("default");
    }


    public static  String fetchCardColor(int val){
        return cardColor.get(val);
    }

    public static EligibilityCheckResponseDTO eligibilitySorryResponse(){
        EligibilityCheckResponseDTO eligibilityCheckResponseDTO = new EligibilityCheckResponseDTO();

        eligibilityCheckResponseDTO.setStatus("SORRY! We are unable to check you eligibility");
        eligibilityCheckResponseDTO.setReason("Please retry after sometime!");

        return eligibilityCheckResponseDTO;
    }

    public static EligibilityCheckResponseDTO eligibilitySuccessResponse(){
        EligibilityCheckResponseDTO eligibilityCheckResponseDTO = new EligibilityCheckResponseDTO();

        eligibilityCheckResponseDTO.setStatus("Congratulation! You are eligible for this form");

        return eligibilityCheckResponseDTO;
    }

    public static EligibilityCheckResponseDTO eligibilityFailedByMaxAge(int year,  int month, int days){

        EligibilityCheckResponseDTO eligibilityCheckResponseDTO = new EligibilityCheckResponseDTO();

        eligibilityCheckResponseDTO.setStatus("SORRY! You are not eligible for this form");

        String reason = MessageFormat.format("Your age is above the eligible age by {0} year {1} month {2} days", year, month, days);
        eligibilityCheckResponseDTO.setReason(reason);

        return eligibilityCheckResponseDTO;
    }

    public static EligibilityCheckResponseDTO eligibilityFailedByMinAge(int year,  int month, int days){
        EligibilityCheckResponseDTO eligibilityCheckResponseDTO = new EligibilityCheckResponseDTO();

        eligibilityCheckResponseDTO.setStatus("SORRY! You are not eligible for this form");

        String reason = MessageFormat.format("Your age is above the eligible age by {0} year {1} month {2} days", year, month, days);
        eligibilityCheckResponseDTO.setReason(reason);

        return eligibilityCheckResponseDTO;
    }

    public static int getRelaxedYear(ApplicationAgeDetails ageDetails, String cateGory){

        return switch (cateGory) {
            case "OBC" -> ageDetails.getObcAge() != null ? ageDetails.getObcAge() : 0;
            case "ST" -> ageDetails.getStAge() != null ? ageDetails.getStAge() : 0;
            case "SC" -> ageDetails.getScAge() != null ? ageDetails.getScAge() : 0;
            case "GEN" -> ageDetails.getGeneralAge() != null ? ageDetails.getGeneralAge() : 0;
            case "EX-ARMY" -> ageDetails.getExArmy() != null ? ageDetails.getExArmy() : 0;
            case "FEMALE" -> ageDetails.getFemaleAge() != null ? ageDetails.getFemaleAge() : 0;
            default -> 0;
        };

    }

    public static Date getAgeAfterRelaxed(Date normalDob,ApplicationAgeDetails ageDetails,
                                          String category){

        int relaxedYrs = 0;
        if(StringUtil.notEmpty(category)) {
            relaxedYrs = getRelaxedYear(ageDetails, category);
        }
        return DateUtils.addYears(normalDob, -relaxedYrs);
    }

}
