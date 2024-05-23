package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.ExamMetaData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExamMetaDataRepository extends CrudRepository<ExamMetaData, String> {
    List<ExamMetaData> findAll();
}
