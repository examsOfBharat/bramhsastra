package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.AdmitCard;
import com.examsofbharat.bramhsastra.prithvi.entity.ResultDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ResultDetailsRepository extends CrudRepository<ResultDetails, String> {

    Page<ResultDetails> findAll(Pageable pageable);

    List<ResultDetails> findByResultDateAfterOrderByResultDateDesc(Date resultDate);
}
