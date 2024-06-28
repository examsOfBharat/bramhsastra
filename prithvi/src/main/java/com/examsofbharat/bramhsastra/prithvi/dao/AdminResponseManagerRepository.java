package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.AdminResponseManager;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminResponseManagerRepository extends CrudRepository<AdminResponseManager, String> {

    List<AdminResponseManager> findAllByStatusOrderByDateCreatedDesc(String status);
}
