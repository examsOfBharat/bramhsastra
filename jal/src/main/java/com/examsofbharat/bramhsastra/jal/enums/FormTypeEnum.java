package com.examsofbharat.bramhsastra.jal.enums;

public enum FormTypeEnum {

    LATEST_FORMS("ALL LATEST FORMS"),
    OLDER_FORMS("FORM EXPIRING SOON"),
    QUALIFICATION_BASED("FORMS BASED ON QUALIFICATION"),
    SECTOR_BASED("FORMS BASED ON SECTOR"),
    GRADE_BASED("FORMS BASED ON GRADE"),
    FEMALE("Family"),
    PROVINCIAL_BASED("FORMS BASED ON REGION"),
    ADMIT("ADMIT CARD"),
    RESULT("RESULT"),
    ANS_KEY("ANSWER KEY");

    private String val;

    FormTypeEnum(String val) {
        this.val = val;
    }

    public String getVal(){ return val; }
}
