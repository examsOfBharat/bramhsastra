package com.examsofbharat.bramhsastra.jal.enums;

public enum FormSectorEnum {
    BANKING("Banking Job"),
    RAILWAY("Railway Job"),
    SSC_CENTRAL("Cental SSC Job"),
    CIVIL_SERVICES("Cibil service (UPSC)"),
    DEFENSE_SERVICE("Defence Job"),
    PSU_JOB("Indian PSU Job"),
    MANAGEMENT("Management Job"),
    STATE_POLICE("State police job"),
    AGRICULTURE("Agricultral Job"),
    LAW("Job in Law");

    private String val;

    FormSectorEnum(String val) {
        this.val = val;
    }

    public String getVal(){ return val; }
}
