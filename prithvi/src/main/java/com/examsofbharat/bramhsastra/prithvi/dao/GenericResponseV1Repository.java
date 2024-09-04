package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.GenericResponseV1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface GenericResponseV1Repository extends CrudRepository<GenericResponseV1, String> {


    Page<GenericResponseV1> findByType(Pageable pageable, String type);

    Optional<GenericResponseV1> findById(String admitId);

    GenericResponseV1 findByAppIdRefAndType(String appId, String type);

    List<GenericResponseV1> findByTypeAndDateCreatedAfterOrderByDateCreatedDesc(String type, Date admitDate);

}
