package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.AdmitCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface AdmitCardRepository extends CrudRepository<AdmitCard, String> {

    Page<AdmitCard> findAll(Pageable pageable);
}
