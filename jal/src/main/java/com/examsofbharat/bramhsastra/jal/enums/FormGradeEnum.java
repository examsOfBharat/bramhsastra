package com.examsofbharat.bramhsastra.jal.enums;

public enum FormGradeEnum {
    A_GRADE("Grade A Job"),
    B_GRADE("Grade B Job"),
    C_GRADE("Grade C Job"),
    D_GRADE("Grade D Job");

    private String val;

    FormGradeEnum(String val) {
        this.val = val;
    }

    public String getVal(){ return val; }
}
