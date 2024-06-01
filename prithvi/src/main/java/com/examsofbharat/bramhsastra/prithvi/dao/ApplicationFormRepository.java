package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationForm;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApplicationFormRepository extends CrudRepository<ApplicationForm, String> {

    Optional<ApplicationForm> findById(String appId);
}
