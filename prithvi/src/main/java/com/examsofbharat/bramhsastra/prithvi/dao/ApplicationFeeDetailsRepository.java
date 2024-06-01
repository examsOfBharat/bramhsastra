package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationFeeDatails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApplicationFeeDetailsRepository extends CrudRepository<ApplicationFeeDatails, String> {

    Optional<ApplicationFeeDatails> findTopByAppIdRefOrderByDateCreatedDesc(String applicationId);
}
