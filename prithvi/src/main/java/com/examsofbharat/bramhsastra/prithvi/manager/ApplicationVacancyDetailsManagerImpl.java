package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationContentManagerRepository;
import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationVacancyDetailsRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationVacancyDetails;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationVacancyDetailsManagerImpl extends GenericManager<ApplicationVacancyDetails, String> {
    ApplicationVacancyDetailsRepository applicationVacancyDetailsRepository;

    @Autowired
    public ApplicationVacancyDetailsManagerImpl(ApplicationVacancyDetailsRepository applicationVacancyDetailsRepository, ApplicationContentManagerRepository applicationContentManagerRepository){
        this.applicationVacancyDetailsRepository = applicationVacancyDetailsRepository;
        setCrudRepository(applicationVacancyDetailsRepository);
    }

    public List<ApplicationVacancyDetails> getAllVacancyById(String id){
        return applicationVacancyDetailsRepository.findAllByAppIdRefOrderByDateCreatedDesc(id);
    }

    public ApplicationVacancyDetails getVacancyByAppId(String appId){
        return applicationVacancyDetailsRepository.findByAppIdRefOrderByDateCreatedDesc(appId);
    }

}
