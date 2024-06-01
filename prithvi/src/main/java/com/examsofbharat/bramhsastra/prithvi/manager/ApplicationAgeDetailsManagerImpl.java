package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationAgeDetailsRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationAgeDetails;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationAgeDetailsManagerImpl extends GenericManager<ApplicationAgeDetails, String> {

    ApplicationAgeDetailsRepository applicationAgeDetailsRepository;

    @Autowired
    public ApplicationAgeDetailsManagerImpl(ApplicationAgeDetailsRepository applicationAgeDetailsRepository) {
        this.applicationAgeDetailsRepository = applicationAgeDetailsRepository;
        setCrudRepository(applicationAgeDetailsRepository);
    }

     public ApplicationAgeDetails getApplicationAgeDetailsById(String id) {
        return applicationAgeDetailsRepository.findTopByAppIdRefOrderByDateCreatedDesc(id).orElse(null);
     }
}
