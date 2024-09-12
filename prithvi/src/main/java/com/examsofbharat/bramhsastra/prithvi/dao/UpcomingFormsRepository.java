package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.UpcomingForms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface UpcomingFormsRepository extends CrudRepository<UpcomingForms, String> {

    Page<UpcomingForms> findAll(Pageable pageable);

    List<UpcomingForms> findByDateCreatedAfterOrderByDateCreatedDesc(Date dateCreated);
}
