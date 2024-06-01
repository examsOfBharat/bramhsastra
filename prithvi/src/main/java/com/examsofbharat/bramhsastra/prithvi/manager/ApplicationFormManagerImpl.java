package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationFormRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationForm;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationFormManagerImpl extends GenericManager<ApplicationForm, String> {
    ApplicationFormRepository applicationFormRepository;

    @Autowired
    public ApplicationFormManagerImpl(ApplicationFormRepository applicationFormRepository) {
        this.applicationFormRepository = applicationFormRepository;
        setCrudRepository(applicationFormRepository);
    }

    public ApplicationForm getApplicationFormById(String id) {
        return applicationFormRepository.findById(id).orElse(null);
    }
}
