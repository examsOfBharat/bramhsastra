package com.examsofbharat.bramhsastra.jal.enums;

public enum FormSubTypeEnum {

    TENTH("For 10Th pass"),
    DIPLOMA("For diploma"),
    TWELFTH("For 12th Pass"),
    GRADUATE("For Graduate"),
    ABOVE_GRADUATE("For above graduate"),

    A_GRADE("Grade A Job"),
    B_GRADE("Grade B Job"),
    C_GRADE("Grade C Job"),
    D_GRADE("Grade D Job"),

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

    STATE("State Govt Job"),
    CENTRAL("Central Govt Job"),

    ADMIT("Admit card"),
    RESULT("Result job");

    private String val;

    FormSubTypeEnum(String val) {
        this.val = val;
    }

    public String getVal(){ return val; }
}
