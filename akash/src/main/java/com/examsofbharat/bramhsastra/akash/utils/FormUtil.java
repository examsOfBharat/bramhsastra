package com.examsofbharat.bramhsastra.akash.utils;

import com.examsofbharat.bramhsastra.akash.constants.AkashConstants;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.examsofbharat.bramhsastra.akash.constants.AkashConstants.GREEN_COLOR;
import static com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum.*;
import static com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum.ADMIT;
import static com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum.RESULT;
import static com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum.*;

@Slf4j
@Component
public class FormUtil {

    public final static Map<FormSubTypeEnum, FormTypeEnum> formSubTypeMap = new HashMap<>();
    public static List<String> cardColor = new ArrayList<>();

    @PostConstruct
    public void init() {
        initFormTypeMap();
        initVacancyColor();
    }

    public static String getLastXDaysDateColor(Date date){
        if(date == null) return null;
        boolean flag = DateUtils.isTimePassedDays(5, date);
        if(flag)
            return "#FF0000";
        return GREEN_COLOR;
    }

    public static String getFormShowDateColor(Date showDate){
        boolean flag = DateUtils.isTimePassedDays(3, showDate);

        if(flag)
            return GREEN_COLOR;
        return "#ff0000";
    }

    public static String formatExamDate(Date examDate){
        if(examDate.compareTo(new Date()) < 0){
            return "EXAM DONE";
        }
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

    public static FormTypeEnum getFormType(FormSubTypeEnum subType){
        return formSubTypeMap.get(subType);
    }

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
            postDataList.add("#0000FF");
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

    public void initVacancyColor(){
        cardColor.add("#fef9ec");
        cardColor.add("#e8f1fb");
        cardColor.add("#f4f2f7");
        cardColor.add("#e6f4ec");
    }

    public static  String fetchCardColor(int val){
        return cardColor.get(val);
    }

}
