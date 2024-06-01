package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationContentManagerRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationContentManager;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationContentManagerManagerImpl extends GenericManager<ApplicationContentManager, String> {

    ApplicationContentManagerRepository applicationContentManagerRepository;

    @Autowired
    public ApplicationContentManagerManagerImpl(ApplicationContentManagerRepository applicationContentManagerRepository) {
        this.applicationContentManagerRepository = applicationContentManagerRepository;
        setCrudRepository(applicationContentManagerRepository);
    }

    public ApplicationContentManager getApplicationAgeDetailsById(String id) {
        return applicationContentManagerRepository.findTopByAppIdRefOrderByDateCreatedDesc(id).orElse(null);
    }

    public List<ApplicationContentManager> getAllContentById(String id) {
        return applicationContentManagerRepository.findAllByAppIdRefOrderByDateCreatedDesc(id);
    }
}
