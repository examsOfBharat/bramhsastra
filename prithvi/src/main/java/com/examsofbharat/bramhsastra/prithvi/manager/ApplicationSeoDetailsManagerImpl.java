package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationSeoDetailsRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationSeoDetails;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSeoDetailsManagerImpl extends GenericManager<ApplicationSeoDetails, String> {

    ApplicationSeoDetailsRepository applicationSeoDetailsRepository;

    @Autowired
    public ApplicationSeoDetailsManagerImpl(ApplicationSeoDetailsRepository applicationSeoDetailsRepository) {
        this.applicationSeoDetailsRepository = applicationSeoDetailsRepository;
        setCrudRepository(applicationSeoDetailsRepository);
    }

    public ApplicationSeoDetails getSeoDetails(String appId){
        return applicationSeoDetailsRepository.findByAppIdRefOrderByDateCreatedDesc(appId);
    }
}
