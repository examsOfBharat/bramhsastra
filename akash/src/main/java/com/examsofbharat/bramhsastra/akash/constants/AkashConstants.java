package com.examsofbharat.bramhsastra.akash.constants;

import com.examsofbharat.bramhsastra.jal.dto.response.UrlManagerDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class
AkashConstants {
    public static final String COMMA_DELIMETER = ",";
    public static final String EXAMS_OF_BHARAT = "examsOfBhart";
    public static final String SIGN_IN = "signIn";
    public static final String SIGN_UP = "signUp";
    public static final String DATE_CREATED = "dateCreated";
    public static final String DATE_MODIFIED = "dateModified";
    public static final String X_DAYS = "+ days";
    public static final String AGE_RELAXATION_TITLE ="Max/Last Age";
    public static final String FEE_TITLE = "Application Fees";
    public static final String DATES_TITLE = "Dates";

    public static final String GREEN_COLOR = "#03571a";
    public static final String BLUE_COLOR = "#03571a";
    public static final String RED_COLOR = "#870f07";
    public static final String BLUE_COLOR_CODE = "#0000FF";
    public static final String RUPEE_SYMBOL = "₹";
    public static final String YEAR_SYMBOL = " yrs";

    public static final String ADMIT_KEY = "ADMIT";
    public static final String ADMIT_TYPE = "admit";

    public static final String RESULT_KEY = "RESULT";
    public static final String RESULT_TYPE = "result";

    public static void main(String[] args) throws JsonProcessingException {
        UrlManagerDTO urlManagerDTO = new UrlManagerDTO();
        urlManagerDTO.setKey("Bibhu");
        urlManagerDTO.setValue("google.com");

        UrlManagerDTO urlManagerDTO1 = new UrlManagerDTO();
        urlManagerDTO1.setKey("Bibhu");
        urlManagerDTO1.setValue("google.com");

        List<UrlManagerDTO> list = new ArrayList<>();
        list.add(urlManagerDTO);
        list.add(urlManagerDTO1);

        ObjectMapper objectMapper = new ObjectMapper();
        String data = objectMapper.writeValueAsString(list);
        System.out.println(objectMapper.writeValueAsString(list));

        List<UrlManagerDTO> urlManagerDTOList = objectMapper.readValue(data,
                objectMapper.getTypeFactory().constructCollectionType(List.class, UrlManagerDTO.class));

        System.out.println(urlManagerDTOList);
    }

}
