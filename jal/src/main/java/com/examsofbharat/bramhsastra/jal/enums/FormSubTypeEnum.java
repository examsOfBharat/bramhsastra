package com.examsofbharat.bramhsastra.jal.enums;

public enum FormSubTypeEnum {

    TENTH("Forms For 10th pass"),
    DIPLOMA("Forms For Diploma"),
    TWELFTH("Forms For 12th Pass"),
    GRADUATE("Forms For Graduate"),
    ABOVE_GRADUATE("Forms For above graduate"),

    A_GRADE("Grade A Job Forms"),
    B_GRADE("Grade B Job Forms"),
    C_GRADE("Grade C Job Forms"),
    D_GRADE("Grade D Job Forms"),

    BANKING("Banking Job"),
    RAILWAY("Railway Job"),
    SSC_CENTRAL("Central SSC Job"),
    CIVIL_SERVICES("Civil service (UPSC)"),
    DEFENSE_SERVICE("Defence Job"),
    PSU_JOB("Indian PSU Job"),
    MANAGEMENT("Management Job"),
    STATE_POLICE("State police job"),
    AGRICULTURE("Agricultural Job"),
    LAW("Job in Law"),

    STATE("State Government Job"),
    CENTRAL("Central Government Job"),

    ADMIT("Admit card"),
    RESULT("Result job");

    private String val;

    FormSubTypeEnum(String val) {
        this.val = val;
    }

    public String getVal(){ return val; }
}
