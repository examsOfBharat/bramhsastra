package com.examsofbharat.bramhsastra.akash.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Autowired
    EobInitilizer eobInitilizer;

    public String getMailBodyByStatus(String status){
        if(status.equalsIgnoreCase("PENDING")){
            return eobInitilizer.getPendingMailBody();
        } else if (status.equalsIgnoreCase("APPROVED")) {
                return eobInitilizer.getApprovedMailBody();
        }
        return eobInitilizer.getRejectedMailBody();
    }
}
