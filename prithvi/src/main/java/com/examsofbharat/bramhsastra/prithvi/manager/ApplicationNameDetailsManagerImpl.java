package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationNameDetailsRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationNameDetails;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationNameDetailsManagerImpl extends GenericManager<ApplicationNameDetails, String> {
    ApplicationNameDetailsRepository applicationNameDetailsRepository;

    @Autowired
    public ApplicationNameDetailsManagerImpl(ApplicationNameDetailsRepository applicationNameDetailsRepository) {
        this.applicationNameDetailsRepository = applicationNameDetailsRepository;
        setCrudRepository(applicationNameDetailsRepository);
    }

    public List<ApplicationNameDetails> fetchAllAppName(){
        Iterable<ApplicationNameDetails> applicationList =  applicationNameDetailsRepository.findAll();

        List<ApplicationNameDetails> applicationNameDetailsList = new ArrayList<>();
        for (ApplicationNameDetails applicationNameDetails : applicationList) {
            applicationNameDetailsList.add(applicationNameDetails);
        }
        return applicationNameDetailsList;
    }

    public ApplicationNameDetails findNameByAppId(String appId){
        return applicationNameDetailsRepository.findByAppIdRefOrderByDateCreatedDesc(appId);
    }
}
