package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationEligibilityRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationEligibilityDetails;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationEligibilityManagerImpl extends GenericManager<ApplicationEligibilityDetails, String> {

    ApplicationEligibilityRepository applicationEligibilityRepository;

    @Autowired
    public ApplicationEligibilityManagerImpl(ApplicationEligibilityRepository applicationEligibilityRepository) {
        this.applicationEligibilityRepository = applicationEligibilityRepository;
        setCrudRepository(applicationEligibilityRepository);
    }

    public List<ApplicationEligibilityDetails> findAllAppByAppId(String appId){
        return applicationEligibilityRepository.findAllByAppIdRef(appId);
    }

}
