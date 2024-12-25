package com.examsofbharat.bramhsastra.jal.enums;

public enum FormSubTypeEnum {

    TENTH("10th"),
    DIPLOMA("Diploma"),
    TWELFTH("12th"),
    GRADUATE("Graduate"),
    ABOVE_GRADUATE("Post Graduate"),

    A_GRADE("Grade A Job Forms"),
    B_GRADE("Grade B Job Forms"),
    C_GRADE("Grade C Job Forms"),
    D_GRADE("Grade D Job Forms"),

    BANKING("Banking"),
    RAILWAY("Railway"),
    SSC_CENTRAL("Central SSC"),
    CIVIL_SERVICES("Civil service (UPSC)"),
    DEFENSE_SERVICE("Defence"),
    PSU_JOB("Indian PSU Job"),
    STATE_POLICE("State police"),
    LAW("Job in Law"),
    PCS("State PCS"),
    AGRICULTURE("Agricultural"),
    MANAGEMENT("Management"),
    STATE_EXAM("State exams"),
    TEACHING("Teacher exam"),
    APPRENTICES("APPRENTICES"),
    ENTRANCE_EXAM("Entrance Exams"),
    HEATH_PARAMEDIC("Health And Paramedical"),
    ENGINEERING("Engineering"),
    OTHERS("Others"),

    STATE("State Government Job"),
    CENTRAL("Central Government Job"),

    ADMIT("Admit card"),
    RESULT("Result job"),
    ANS_KEY("Answer key");

    private String val;

    FormSubTypeEnum(String val) {
        this.val = val;
    }

    public String getVal(){ return val; }
}
