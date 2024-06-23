package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationVacancyDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationVacancyDetailsRepository extends CrudRepository<ApplicationVacancyDetails, String> {

    List<ApplicationVacancyDetails> findAllByAppIdRefOrderByDateCreatedDesc(String Id);

    ApplicationVacancyDetails findByAppIdRefOrderByDateCreatedDesc(String appId);


}
