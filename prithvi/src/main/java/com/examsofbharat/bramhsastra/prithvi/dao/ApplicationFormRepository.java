package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.Optional;

public interface ApplicationFormRepository extends CrudRepository<ApplicationForm, String> {

    Optional<ApplicationForm> findById(String appId);

    Page<ApplicationForm> findAllByMinQualification(String minQualification, Pageable pageable);

    Page<ApplicationForm> findAllBySectors(String sector, Pageable pageable);

    Page<ApplicationForm> findAllByGrade(String grade, Pageable pageable);

    Page<ApplicationForm> findAllByState(String province, Pageable pageable);

    Page<ApplicationForm> findAllByGender(String province, Pageable pageable);

    ApplicationForm findByMinQualification(String minQualification);

    Page<ApplicationForm> findAll(Pageable pageable);

    Page<ApplicationForm> findByEndDateAfter(Pageable pageable, Date endDate);

    Page<ApplicationForm> findByEndDateBetween(Date startDate, Date endDate, Pageable pageable);

    Page<ApplicationForm> findByAnswerDateAfter(Date startDate, Pageable pageable);

    Page<ApplicationForm> findByDateCreatedAfter(Date startDate, Pageable pageable);


}
