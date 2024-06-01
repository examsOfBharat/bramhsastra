package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationFeeDetailsRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationContentManager;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationFeeDatails;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ApplicationFeeDetailsManagerImpl extends GenericManager<ApplicationFeeDatails, String> {

    ApplicationFeeDetailsRepository applicationFeeDetailsRepository;

    @Autowired
    public ApplicationFeeDetailsManagerImpl(ApplicationFeeDetailsRepository applicationFeeDetailsRepository) {
        this.applicationFeeDetailsRepository = applicationFeeDetailsRepository;
        setCrudRepository(applicationFeeDetailsRepository);
    }

    public ApplicationFeeDatails getApplicationAgeDetailsById(String id) {
        return applicationFeeDetailsRepository.findTopByAppIdRefOrderByDateCreatedDesc(id).orElse(null);
    }
}
