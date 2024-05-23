package com.examsofbharat.bramhsastra.jal.enums;

public enum FormLevelEnum {
    TENTH("For 10Th pass"),
    DIPLOMA("For diploma"),
    TWELFTH("For 12th Pass"),
    GRADUATE("For Graduate"),
    ABOVE_GRADUATE("For above graduate");

    private String val;

    FormLevelEnum(String val) {
        this.val = val;
    }

    public String getVal(){ return val; }


}
