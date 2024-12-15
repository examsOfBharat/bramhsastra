package com.examsofbharat.bramhsastra.jal.enums;

public enum FormTypeEnum {

    LATEST_FORMS("All Latest Forms"),
    OLDER_FORMS("Form Expiring Soon"),
    UPDATES("General Updates"),
    QUALIFICATION_BASED("Forms Based On Qualification"),
    SECTOR_BASED("Forms Based On Sectors"),
    GRADE_BASED("Forms Based On Grades"),
    FEMALE("Family"),
    PROVINCIAL_BASED("Forms Based On Region"),
    ADMIT("Admit Card"),
    RESULT("Result"),
    ANS_KEY("Answer Key");

    private String val;

    FormTypeEnum(String val) {
        this.val = val;
    }

    public String getVal(){ return val; }
}
