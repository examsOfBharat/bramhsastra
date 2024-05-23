package com.examsofbharat.bramhsastra.jal.enums;

public enum FormTypeEnum {
    ADMIT("Recent Admit card 2024"),
    RESULT("Recent Result 2024"),
    QUALIFICATION_BASED("Application based on Qualification"),
    SECTOR_BASED("Based on Sector"),
    GRADE_BASED("Based on Grade"),
    FEMALE("Family"),
    PROVINCIAL_BASED("Based on provincial");

    private String val;

    FormTypeEnum(String val) {
        this.val = val;
    }

    public String getVal(){ return val; }
}
