package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.SystemProperty;
import org.springframework.data.repository.CrudRepository;

public interface SystemPropertyRepository extends CrudRepository<SystemProperty, String> {

    SystemProperty findOneByName(String name);
}
