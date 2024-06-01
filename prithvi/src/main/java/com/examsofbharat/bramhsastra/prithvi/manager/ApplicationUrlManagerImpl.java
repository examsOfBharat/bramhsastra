package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationUrlRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationFeeDatails;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationUrl;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUrlManagerImpl extends GenericManager<ApplicationUrl, String> {

    ApplicationUrlRepository applicationUrlRepository;

    @Autowired
    public ApplicationUrlManagerImpl(ApplicationUrlRepository applicationUrlRepository) {
        this.applicationUrlRepository = applicationUrlRepository;
        setCrudRepository(applicationUrlRepository);
    }

    public ApplicationUrl getApplicationUrlDetailsById(String id) {
        return applicationUrlRepository.findTopByAppIdRefOrderByDateCreatedDesc(id).orElse(null);
    }
}
