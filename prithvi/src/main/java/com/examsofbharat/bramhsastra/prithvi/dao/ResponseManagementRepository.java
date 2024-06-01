package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.ResponseManagement;
import org.springframework.data.repository.CrudRepository;

public interface ResponseManagementRepository extends CrudRepository<ResponseManagement, String> {

    ResponseManagement findByResponseTypeOrderByDateCreatedDesc(String responseType);
}
