package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationUrl;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApplicationUrlRepository extends CrudRepository<ApplicationUrl, String> {
    Optional<ApplicationUrl>findTopByAppIdRefOrderByDateCreatedDesc(String appId);
}
