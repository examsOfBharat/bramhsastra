package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationEligibilityDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationEligibilityRepository extends CrudRepository<ApplicationEligibilityDetails, String> {

    List<ApplicationEligibilityDetails> findAllByAppIdRef(String appId);
}
