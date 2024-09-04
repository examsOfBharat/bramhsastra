package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.GenericContentManager;
import org.springframework.data.repository.CrudRepository;

public interface GenericContentManagerRepository extends CrudRepository<GenericContentManager, String> {

    GenericContentManager findByFormIdRef(String formIdRef);
}
