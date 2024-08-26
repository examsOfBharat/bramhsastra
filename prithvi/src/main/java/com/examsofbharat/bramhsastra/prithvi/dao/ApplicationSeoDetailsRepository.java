package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationSeoDetails;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationSeoDetailsRepository extends CrudRepository<ApplicationSeoDetails, String> {

    ApplicationSeoDetails findByAppIdRefOrderByDateCreatedDesc(String appId);
}
