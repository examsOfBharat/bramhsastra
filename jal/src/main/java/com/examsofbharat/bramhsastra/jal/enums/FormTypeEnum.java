package com.examsofbharat.bramhsastra.jal.enums;

public enum FormTypeEnum {

    LATEST_FORMS("All latest forms"),
    OLDER_FORMS("Forms close to expiry"),
    QUALIFICATION_BASED("Application based on Qualification"),
    SECTOR_BASED("Based on Sector"),
    GRADE_BASED("Based on Grade"),
    FEMALE("Family"),
    PROVINCIAL_BASED("Based on Provincial/Region"),
    ADMIT("Recent Admit card"),
    RESULT("Recent Result");

    private String val;

    FormTypeEnum(String val) {
        this.val = val;
    }

    public String getVal(){ return val; }
}
