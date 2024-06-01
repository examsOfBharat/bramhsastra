package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationContentManager;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationContentManagerRepository extends CrudRepository<ApplicationContentManager, String> {

    Optional<ApplicationContentManager> findTopByAppIdRefOrderByDateCreatedDesc(String applicationId);

    List<ApplicationContentManager> findAllByAppIdRefOrderByDateCreatedDesc(String applicationId);
}
