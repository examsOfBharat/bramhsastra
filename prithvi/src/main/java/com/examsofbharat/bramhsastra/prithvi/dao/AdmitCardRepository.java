package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.AdmitCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AdmitCardRepository extends CrudRepository<AdmitCard, String> {

    Page<AdmitCard> findAll(Pageable pageable);

    Optional<AdmitCard> findById(String admitId);

    AdmitCard findByAppIdRef(String appId);

    List<AdmitCard> findByAdmitCardDateAfterOrderByAdmitCardDateDesc(Date admitDate);

    List<AdmitCard> findByDateCreatedAfterOrderByDateCreatedDesc(Date admitDate);

}
