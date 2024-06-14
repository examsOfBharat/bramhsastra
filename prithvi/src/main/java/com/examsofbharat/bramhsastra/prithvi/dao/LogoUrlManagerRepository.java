package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.LogoUrlManager;
import org.springframework.data.repository.CrudRepository;

public interface LogoUrlManagerRepository extends CrudRepository<LogoUrlManager, String> {

    LogoUrlManager findByName(String name);
}
