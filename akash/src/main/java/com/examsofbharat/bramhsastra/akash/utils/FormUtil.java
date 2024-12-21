package com.examsofbharat.bramhsastra.akash.utils;

import com.examsofbharat.bramhsastra.akash.constants.AkashConstants;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationEligibilityDTO;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationVacancyDTO;
import com.examsofbharat.bramhsastra.jal.dto.SecondPageSeoDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.EligibilityCheckResponseDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.LandingSectionDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.UrlManagerDTO;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationAgeDetails;
//import com.ibm.icu.text.NumberFormat;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationForm;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.examsofbharat.bramhsastra.akash.constants.AkashConstants.*;
import static com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum.*;
import static com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum.ADMIT;
import static com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum.RESULT;
import static com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum.*;
import static com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum.ANS_KEY;

@Slf4j
@Component
public class FormUtil {

    public final static Map<String, FormTypeEnum> formSubTypeMap = new HashMap<>();
    public static final List<String> sectorFormTypeList = new ArrayList<>();
    public static final List<String> gradeTypeList = new ArrayList<>();
    public static List<String> cardColor = new ArrayList<>();
    public static List<String> newCardColor = new ArrayList<>();
    public static List<String> new2CardColor = new ArrayList<>();
    public static List<String> cardSecColor = new ArrayList<>();
    public static Map<String, String> secondPageTitleMap =  new HashMap<>();
    public static Map<String,SecondPageSeoDetailsDTO> secondPageSeoMap = new HashMap<>();
    public static Map<String, String> qualificationName = new HashMap<>();

    public static Map<String,String> cacheData = new HashMap<>();
    public static Map<String, String> formCache = new ConcurrentHashMap<>();
    public static Map<String, String> genericResCache = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        initFormTypeMap();
        initVacancyColor();
        initNewVacancyColor();
        initNew2VacancyColor();
        initSecColor();
        initRelatedFormType();
        initSecondPageTitle();
        initGradeList();
        initQualificationName();
        initSecondPageSeo();
    }

    public void initQualificationName(){
        qualificationName.put(TENTH.name(), "10TH");
        qualificationName.put(TWELFTH.name(), "12TH");
        qualificationName.put(GRADUATE.name(), "Graduate");
        qualificationName.put(DIPLOMA.name(), "Diploma");
        qualificationName.put(ABOVE_GRADUATE.name(), "PG or above Graduate");
    }

    public static String getLastXDaysDateColor(Date date){
        if(date == null) return null;
        boolean flag = DateUtils.isTimePassedDays(5, date);
        if(flag)
            return BLUE_COLOR_CODE;
        return GREEN_COLOR;
    }

    public static boolean dateIsWithinXDays(Date date){
        if(date == null) return false;

        Date todayDate = new Date();
        return date.after(DateUtils.addDays(todayDate,-8));
    }

    public static boolean dateIsWithin2Days(Date date){
        if(date == null) return false;

        Date todayDate = new Date();
        return date.after(DateUtils.addDays(todayDate,-2));
    }


    public static String getExpiryDateColor(Date date){
        if(date == null) return null;

        boolean flag = DateUtils.isDateIsAfterXDaysNow(5, date);
        if(flag)
            return GREEN_COLOR;
        return RED_COLOR;
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
        formSubTypeMap.put(ADMIT.name(), FormTypeEnum.ADMIT);
        formSubTypeMap.put(RESULT.name(), FormTypeEnum.RESULT);
        formSubTypeMap.put(FormSubTypeEnum.ANS_KEY.name(), ANS_KEY);

        formSubTypeMap.put(TENTH.name(), QUALIFICATION_BASED);
        formSubTypeMap.put(TWELFTH.name(), QUALIFICATION_BASED);
        formSubTypeMap.put(DIPLOMA.name(), QUALIFICATION_BASED);
        formSubTypeMap.put(GRADUATE.name(), QUALIFICATION_BASED);
        formSubTypeMap.put(ABOVE_GRADUATE.name(), QUALIFICATION_BASED);


        formSubTypeMap.put(BANKING.name(), SECTOR_BASED);
        formSubTypeMap.put(PSU_JOB.name(), SECTOR_BASED);
        formSubTypeMap.put(AGRICULTURE.name(), SECTOR_BASED);
        formSubTypeMap.put(DEFENSE_SERVICE.name(), SECTOR_BASED);
        formSubTypeMap.put(STATE_POLICE.name(), SECTOR_BASED);
        formSubTypeMap.put(CIVIL_SERVICES.name(), SECTOR_BASED);
        formSubTypeMap.put(MANAGEMENT.name(),SECTOR_BASED);
        formSubTypeMap.put(RAILWAY.name(), SECTOR_BASED);
        formSubTypeMap.put(SSC_CENTRAL.name(), SECTOR_BASED);
        formSubTypeMap.put(LAW.name(), SECTOR_BASED);
        formSubTypeMap.put(PCS.name(), SECTOR_BASED);
        formSubTypeMap.put(STATE_EXAM.name(), SECTOR_BASED);
        formSubTypeMap.put(TEACHING.name(), SECTOR_BASED);
        formSubTypeMap.put(APPRENTICES.name(), SECTOR_BASED);
        formSubTypeMap.put(ENTRANCE_EXAM.name(), SECTOR_BASED);
        formSubTypeMap.put(OTHERS.name(),SECTOR_BASED);


        formSubTypeMap.put(A_GRADE.name(), GRADE_BASED);
        formSubTypeMap.put(B_GRADE.name(), GRADE_BASED);
        formSubTypeMap.put(C_GRADE.name(), GRADE_BASED);
        formSubTypeMap.put(D_GRADE.name(), GRADE_BASED);

        //for central
        formSubTypeMap.put(CENTRAL.name(), PROVINCIAL_BASED);

        //for state
        formSubTypeMap.put("AP",PROVINCIAL_BASED);
        formSubTypeMap.put("ARP",PROVINCIAL_BASED);
        formSubTypeMap.put("ASSAM",PROVINCIAL_BASED);
        formSubTypeMap.put("BIHAR",PROVINCIAL_BASED);
        formSubTypeMap.put("CHHATTISGARH",PROVINCIAL_BASED);
        formSubTypeMap.put("GOA",PROVINCIAL_BASED);
        formSubTypeMap.put("GUJARAT",PROVINCIAL_BASED);
        formSubTypeMap.put("HARYANA",PROVINCIAL_BASED);
        formSubTypeMap.put("HP",PROVINCIAL_BASED);
        formSubTypeMap.put("J&K",PROVINCIAL_BASED);
        formSubTypeMap.put("JHARKHAND",PROVINCIAL_BASED);
        formSubTypeMap.put("KARNATAKA", PROVINCIAL_BASED);
        formSubTypeMap.put("KERALA",PROVINCIAL_BASED);
        formSubTypeMap.put("MP",PROVINCIAL_BASED);
        formSubTypeMap.put("MAHARASHTRA",PROVINCIAL_BASED);
        formSubTypeMap.put("MANIPUR",PROVINCIAL_BASED);
        formSubTypeMap.put("MEGHALAYA",PROVINCIAL_BASED);
        formSubTypeMap.put("MIZORAM",PROVINCIAL_BASED);
        formSubTypeMap.put("NAGALAND",PROVINCIAL_BASED);
        formSubTypeMap.put("ODISHA",PROVINCIAL_BASED);
        formSubTypeMap.put("PUNJAB",PROVINCIAL_BASED);
        formSubTypeMap.put("RAJASTHAN",PROVINCIAL_BASED);
        formSubTypeMap.put("SIKKIM",PROVINCIAL_BASED);
        formSubTypeMap.put("TN",PROVINCIAL_BASED);
        formSubTypeMap.put("TELANGANA",PROVINCIAL_BASED);
        formSubTypeMap.put("TRIPURA",PROVINCIAL_BASED);
        formSubTypeMap.put("UP",PROVINCIAL_BASED);
        formSubTypeMap.put("UTTARAKHAND",PROVINCIAL_BASED);
        formSubTypeMap.put("WB",PROVINCIAL_BASED);

        log.info(formSubTypeMap.toString());
    }

    public void initVacancyColor(){
        cardColor.add("#fef9ec");
        cardColor.add("#e9f1fb");
        cardColor.add("#f5f2f8");
        cardColor.add("#e7f4ed");
    }

    public void initNewVacancyColor(){
        newCardColor.add("#184688");
        newCardColor.add("#31a05e");
        newCardColor.add("#1d3868");
        newCardColor.add("#4b238d");
    }
    public void initNew2VacancyColor(){
        new2CardColor.add("#F9BBAE");
        new2CardColor.add("#F3ACB4");
        new2CardColor.add("#C794B0");
        new2CardColor.add("#8A7EA4");
        new2CardColor.add("B59DD1");
    }
//
//    public void initVacancyColor(){
//        cardColor.add("#000000");
//        cardColor.add("#000000");
//        cardColor.add("#000000");
//        cardColor.add("#000000");
//    }



    public void initSecColor(){
        cardSecColor.add("#BBDFCD");
        cardSecColor.add("#FEE9B5");
        cardSecColor.add("#D5E3F8");
        cardSecColor.add("#E8E1EE");
    }

    public void initRelatedFormType(){
        sectorFormTypeList.add(BANKING.name());
        sectorFormTypeList.add(LAW.name());
        sectorFormTypeList.add(SSC_CENTRAL.name());
        sectorFormTypeList.add(CIVIL_SERVICES.name());
        sectorFormTypeList.add(DEFENSE_SERVICE.name());
        sectorFormTypeList.add(STATE_POLICE.name());
        sectorFormTypeList.add(MANAGEMENT.name());
        sectorFormTypeList.add(PSU_JOB.name());
        sectorFormTypeList.add(AGRICULTURE.name());
        sectorFormTypeList.add(MANAGEMENT.name());
        sectorFormTypeList.add(RAILWAY.name());
        sectorFormTypeList.add(PCS.name());
        sectorFormTypeList.add(STATE_EXAM.name());
        sectorFormTypeList.add(TEACHING.name());
        sectorFormTypeList.add(APPRENTICES.name());
        sectorFormTypeList.add(ENTRANCE_EXAM.name());
        sectorFormTypeList.add(OTHERS.name());
    }

    public void initGradeList(){
        gradeTypeList.add(A_GRADE.name());
        gradeTypeList.add(B_GRADE.name());
        gradeTypeList.add(C_GRADE.name());
        gradeTypeList.add(D_GRADE.name());
    }

    public void initSecondPageSeo(){
        SecondPageSeoDetailsDTO latesFormSeo = new SecondPageSeoDetailsDTO();
        latesFormSeo.setTitle("Latest Form 2025");
        latesFormSeo.setKeywords("Latest Government Job Forms 2025,Sarkari Naukri Application Form,Latest Sarkari Job Forms,Latest Govt Exam Forms,Latest Sarkari Form Updates,New Sarkari Form");
        latesFormSeo.setDescription("Stay updated with the latest government job application forms for 2025. Find forms for Sarkari Naukri, entrance exams, and more at Exams of Bharat. Filter by qualification, region, and category to apply for the right job easily.");
        secondPageSeoMap.put(LATEST_FORMS.name(), latesFormSeo);

        SecondPageSeoDetailsDTO admitCardSeo = new SecondPageSeoDetailsDTO();
        admitCardSeo.setTitle("Latest Admit card 2025 - examsofbharat");
        admitCardSeo.setKeywords("Latest Government admit card 2025,Sarkari Naukri admit card,Latest admit card,admit card examsofbharat,examsofbharat admitcard Updates,New Sarkari admit card");
        admitCardSeo.setDescription("Stay updated with the latest government job admit card for 2025. Find admit card for Sarkari Naukri, entrance exams, and more at Exams of Bharat.");
        secondPageSeoMap.put(ADMIT.name(), admitCardSeo);

        SecondPageSeoDetailsDTO resultSeo = new SecondPageSeoDetailsDTO();
        resultSeo.setTitle("Latest Result 2025");
        resultSeo.setKeywords("Latest Government Job result 2025,Sarkari Naukri result,Latest result,Latest Govt Exam result,ssc result,upsc result, bpsc result, new result");
        resultSeo.setDescription("Stay updated with the latest government job result for 2025. Find result for Sarkari Naukri, entrance exams, and more at Exams of Bharat.");
        secondPageSeoMap.put(RESULT.name(), resultSeo);

        SecondPageSeoDetailsDTO upcoming = new SecondPageSeoDetailsDTO();
        upcoming.setTitle("Latest Upcoming Vacancy 2025");
        upcoming.setKeywords("Upcoming Government Job 2025,Sarkari Naukri upcoming,new upcoming forms,upcoming Govt Exam,ssc upcoming forms,upsc upcoming vacancy,upcoming vacancy, new upcoming vacancy");
        upcoming.setDescription("Discover all upcoming government job vacancies for 2025 on ExamsofBharat.com. Browse the latest and expected Sarkari Naukri opportunities across various departments and sectors. Stay informed about important dates, eligibility, and job prospects to plan your next career move effectively!");
        secondPageSeoMap.put("UPCOMING", upcoming);

        SecondPageSeoDetailsDTO defaultSeo = new SecondPageSeoDetailsDTO();
        defaultSeo.setTitle("Forms Details");
        defaultSeo.setKeywords("All forms, Forms based on qualification, Form based on sector");
        defaultSeo.setDescription("Exams of bharat is entitled to show all forms updates, entrance exams, admit card and results etc");
        secondPageSeoMap.put("DEFAULT", defaultSeo);
    }

    public static SecondPageSeoDetailsDTO getSecondPageSeo(String subType){
        if(secondPageSeoMap.containsKey(subType)){
            return secondPageSeoMap.get(subType);
        }
        return secondPageSeoMap.get("DEFAULT");
    }


    public void initSecondPageTitle(){
        secondPageTitleMap.put(ADMIT.name(), "All Admit card details");
        secondPageTitleMap.put(RESULT.name(), "All Result Details");

        secondPageTitleMap.put(TENTH.name(), "10TH Level Forms");
        secondPageTitleMap.put(TWELFTH.name(), "12TH Level Forms");
        secondPageTitleMap.put(DIPLOMA.name(), "Diploma Level Forms");
        secondPageTitleMap.put(GRADUATE.name(), "Graduate Level Forms");
        secondPageTitleMap.put(ABOVE_GRADUATE.name(), "Above Graduate Level Forms");


        secondPageTitleMap.put(BANKING.name(), "Banking Forms");
        secondPageTitleMap.put(PSU_JOB.name(), "PSU Forms");
        secondPageTitleMap.put(AGRICULTURE.name(), "Agriculture Forms");
        secondPageTitleMap.put(DEFENSE_SERVICE.name(), "Defence Job Forms");
        secondPageTitleMap.put(STATE_POLICE.name(), "State police Forms");
        secondPageTitleMap.put(CIVIL_SERVICES.name(), "Civil services Forms");
        secondPageTitleMap.put(MANAGEMENT.name(),"Management Forms");
        secondPageTitleMap.put(RAILWAY.name(), "Railway Forms");
        secondPageTitleMap.put(SSC_CENTRAL.name(), "SSC Forms");
        secondPageTitleMap.put(LAW.name(), "Law Forms");
        secondPageTitleMap.put(PCS.name(), "Forms Under PCS");
        secondPageTitleMap.put(STATE_EXAM.name(), "All State exams");
        secondPageTitleMap.put(TEACHING.name(), "All Teacher Exam");
        secondPageTitleMap.put(APPRENTICES.name(), "All latest Apprentices");
        secondPageTitleMap.put(ENTRANCE_EXAM.name(), "All Entrance Exams");
        secondPageTitleMap.put(OTHERS.name(), "Other recent forms");


        secondPageTitleMap.put(A_GRADE.name(), "Grade-A forms");
        secondPageTitleMap.put(B_GRADE.name(), "Grade-B forms");
        secondPageTitleMap.put(C_GRADE.name(), "Grade-C forms");
        secondPageTitleMap.put(D_GRADE.name(), "Grade-D forms");

//for central
        secondPageTitleMap.put(CENTRAL.name(), "Central Govt. Forms");

//for state
        secondPageTitleMap.put("AP","Andhra Pradesh State Forms");
        secondPageTitleMap.put("ARP","Arunachal Pradesh State Forms");
        secondPageTitleMap.put("ASSAM","Assam State Forms");
        secondPageTitleMap.put("BIHAR","Bihar State Forms");
        secondPageTitleMap.put("CHHATTISGARH","Chhatisgarh State Forms");
        secondPageTitleMap.put("GOA","Goa State Forms");
        secondPageTitleMap.put("GUJARAT","Gujrat State Forms");
        secondPageTitleMap.put("HARYANA","Haryana State Forms");
        secondPageTitleMap.put("HP","Himachal Pradesh State Forms");
        secondPageTitleMap.put("J&K","J&K  State Forms");
        secondPageTitleMap.put("JHARKHAND","Jharkhand State Forms");
        secondPageTitleMap.put("KARNATAKA", "Karnataka State Forms");
        secondPageTitleMap.put("KERALA","Kerala State Forms");
        secondPageTitleMap.put("MP","Madhya Pradesh State Forms");
        secondPageTitleMap.put("MAHARASHTRA","Maharashtra State Forms");
        secondPageTitleMap.put("MANIPUR","Manipur State Forms");
        secondPageTitleMap.put("MEGHALAYA","Meghalaya State Forms");
        secondPageTitleMap.put("MIZORAM","Mizoram State Forms");
        secondPageTitleMap.put("NAGALAND","Nagaland State Forms");
        secondPageTitleMap.put("ODISHA","Odisha State Forms");
        secondPageTitleMap.put("PUNJAB","Punjab State Forms");
        secondPageTitleMap.put("RAJASTHAN","Rajsthan State Forms");
        secondPageTitleMap.put("SIKKIM","Sikkim State Forms");
        secondPageTitleMap.put("TN","Tamil Nadu State Forms");
        secondPageTitleMap.put("TELANGANA","Telangana State Forms");
        secondPageTitleMap.put("TRIPURA","Tripura State Forms");
        secondPageTitleMap.put("UP","Uttar Pradesh State Forms");
        secondPageTitleMap.put("UTTARAKHAND","Uttarakhand State Forms");
        secondPageTitleMap.put("WB","West Bengal State Forms");

        secondPageTitleMap.put("LATEST_FORMS", "All Latest Forms");
        secondPageTitleMap.put("OLDER_FORMS", "All Forms whose last date is close");
        secondPageTitleMap.put("ANS_KEY", "All Answer Keys");
    }

    public static FormTypeEnum getFormType(String subType){
        return formSubTypeMap.get(subType);
    }

    public static String getSecondPageTitle(String subType){
        return secondPageTitleMap.get(subType);
    }

    public static List<String> getSectorFormTypeList(){ return sectorFormTypeList; }

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

    //TODO need to optimise this section with above code
    public static String getPngLogoByName(String formName){
        String[] nameList = formName.split(" ");
        String url;
        for(String str : nameList){
            url = EobInitilizer.getPngLogoByName(str);
            if(Objects.nonNull(url)){
                return url;
            }
        }
        return EobInitilizer.getPngLogoByName("default");
    }

    public static String getUrlTitle(ApplicationForm applicationForm){
        return StringUtil.notEmpty(applicationForm.getSortName()) ?
                applicationForm.getSortName() :
                applicationForm.getExamName();
    }


    public static  String fetchCardColor(int val){
        return cardColor.get(val);
    }

    public static  String fetchNewCardColor(int val){
        return newCardColor.get(val);
    }

    public static  String fetchNew2CardColor(int val){
        return new2CardColor.get(val);
    }

    public static String fetchSecCardColor(int val){return cardSecColor.get(val);}

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

    public static String formatIntoIndianNumSystem(int amount){
        try{
            Format format = NumberFormat.getCurrencyInstance(new Locale("en","in"));
            String formatValue = format.format(amount);
            return processStrValue(formatValue).replaceAll("₹","");
        }catch (Exception e){
            log.error("Exception occurred while formating value into indian system");
        }
        return String.valueOf(amount);
    }

    private static String processStrValue(String val){
        String[] splitVal = val.split("\\.");
        if(splitVal.length > 1 && Integer.parseInt(splitVal[1]) > 0)
            return val;
        return splitVal[0];
    }

//    public static void main(String[] args) {
//        int value = 123456;
//        System.out.println(FormUtil.formatIntoIndianNumSystem(value));
//    }


    public static void eligibilityComparator(List<ApplicationEligibilityDTO> list){
        // Sorting the list based on the sequence field
        Collections.sort(list, Comparator.comparingInt(ApplicationEligibilityDTO::getSequence));
    }

    public static void vacancyComparator(List<ApplicationVacancyDTO> list){
        // Sorting the list based on the sequence field
        Collections.sort(list, Comparator.comparingInt(ApplicationVacancyDTO::getSequence));
    }

    public static int genRandomNo(){
        Random random = new Random();
        return random.nextInt(EobInitilizer.getColorCount);
    }

    public static String getUrlTitle(String title){
        return title.trim().
                replace(","," ").
                replace("   "," ").
                replace("  "," ").
                replace(" ","-");
    }

    // Helper method to add URLs safely to the list
    public static void addUrlToList(List<UrlManagerDTO> urlList, String key, String value) {
        if (StringUtil.notEmpty(value)) {
            UrlManagerDTO urlManagerDTO = new UrlManagerDTO();
            urlManagerDTO.setKey(key);
            urlManagerDTO.setValue(value);
            urlList.add(urlManagerDTO);
        }
    }

    public static LandingSectionDTO populateBasicLandingSection(FormTypeEnum formTypeEnum){
        boolean viewAll = (formTypeEnum.equals(FormTypeEnum.ADMIT) ||
                formTypeEnum.equals(FormTypeEnum.RESULT) ||
                formTypeEnum.equals(LATEST_FORMS) ||
                formTypeEnum.equals(OLDER_FORMS));

        return buildSections(0, formTypeEnum, viewAll);
    }

    public static LandingSectionDTO buildSections(int sortIndex, FormTypeEnum formTypeEnum, boolean viewALl) {

        LandingSectionDTO landingSectionDTO = new LandingSectionDTO();
        landingSectionDTO.setType(formTypeEnum);
        landingSectionDTO.setTitle(formTypeEnum.getVal());
        landingSectionDTO.setSortIndex(sortIndex);
        landingSectionDTO.setViewAll(viewALl);

        return landingSectionDTO;
    }


    public static String buildEmailHtml(String recipientName, String submitterName, String formName) {

        String approveLink = "https://examsofbharat-admin.vercel.app/login";
        String rejectLink = "https://examsofbharat-admin.vercel.app/login";

        String htmlTemplate = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Email Template</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Arial', sans-serif;\n" +
                "            line-height: 1.6;\n" +
                "            background-color: #f2f2f2;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #ffffff;\n" +
                "            border: 1px solid #e0e0e0;\n" +
                "            border-radius: 5px;\n" +
                "            box-shadow: 0 0 10px rgba(0,0,0,0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #007bff;\n" +
                "            color: #ffffff;\n" +
                "            text-align: center;\n" +
                "            padding: 10px 0;\n" +
                "            border-radius: 5px 5px 0 0;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .header h2 {\n" +
                "            margin: 0;\n" +
                "            font-size: 24px;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding-bottom: 20px;\n" +
                "        }\n" +
                "        .button {\n" +
                "            align-items: center;\n" +
                "            appearance: none;\n" +
                "            background-image: radial-gradient(100% 100% at 100% 0, #5adaff 0, #5468ff 100%);\n" +
                "            border: 0;\n" +
                "            border-radius: 6px;\n" +
                "            box-shadow: rgba(45, 35, 66, .4) 0 2px 4px, rgba(45, 35, 66, .3) 0 7px 13px -3px, rgba(58, 65, 111, .5) 0 -3px 0 inset;\n" +
                "            box-sizing: border-box;\n" +
                "            color: #fff;\n" +
                "            cursor: pointer;\n" +
                "            display: flex;\n" +
                "            font-family: \"JetBrains Mono\", monospace;\n" +
                "            height: 48px;\n" +
                "            justify-content: center;\n" +
                "            line-height: 1;\n" +
                "            list-style: none;\n" +
                "            overflow: hidden;\n" +
                "            padding-left: 16px;\n" +
                "            padding-right: 16px;\n" +
                "            position: relative;\n" +
                "            text-align: left;\n" +
                "            text-decoration: none;\n" +
                "            transition: box-shadow .15s, transform .15s;\n" +
                "            user-select: none;\n" +
                "            -webkit-user-select: none;\n" +
                "            touch-action: manipulation;\n" +
                "            white-space: nowrap;\n" +
                "            will-change: box-shadow, transform;\n" +
                "            font-size: 18px;\n" +
                "        }\n" +
                "        .button:focus {\n" +
                "            box-shadow: #3c4fe0 0 0 0 1.5px inset, rgba(45, 35, 66, .4) 0 2px 4px, rgba(45, 35, 66, .3) 0 7px 13px -3px, #3c4fe0 0 -3px 0 inset;\n" +
                "        }\n" +
                "        .button:hover {\n" +
                "            box-shadow: rgba(45, 35, 66, .4) 0 4px 8px, rgba(45, 35, 66, .3) 0 7px 13px -3px, #3c4fe0 0 -3px 0 inset;\n" +
                "            transform: translateY(-2px);\n" +
                "        }\n" +
                "        .button:active {\n" +
                "            box-shadow: #3c4fe0 0 3px 7px inset;\n" +
                "            transform: translateY(2px);\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            margin-top: 20px;\n" +
                "            color: #777777;\n" +
                "            font-size: 12px;\n" +
                "        }\n" +
                "        .buttons{\n" +
                "            display: flex ;\n" +
                "            justify-content: center;\n" +
                "            column-gap: 10px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h2>Email Title</h2>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Hey " + recipientName + "</p>\n" +
                "            <p>" + submitterName + " has submitted form <strong>" + formName + "</strong>. A PDF file has been attached for your review.</p>\n" +
                "            <div class=\"buttons\">\n" +
                "                <a href=\"" + approveLink + "\" class=\"button\">Click to Approve</a>\n" +
                "                <a href=\"" + rejectLink + "\" class=\"button reject\">Click to Reject</a>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>This email was sent via Your Company. Please do not reply to this email.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return htmlTemplate;
    }

}
