package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationNameDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationNameDetailsRepository extends CrudRepository<ApplicationNameDetails, String> {

    ApplicationNameDetails findByAppIdRefOrderByDateCreatedDesc(String appId);
}
