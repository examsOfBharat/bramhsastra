package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.UpcomingForms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UpcomingFormsRepository extends CrudRepository<UpcomingForms, String> {

    Page<UpcomingForms> findAll(Pageable pageable);
}
