package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ExamMetaDataRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ExamMetaData;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExamMetaDataManagerImp extends GenericManager<ExamMetaData, String> {
    ExamMetaDataRepository examMetaDataRepository;

    @Autowired
    public ExamMetaDataManagerImp(ExamMetaDataRepository examMetaDataRepository) {
        this.examMetaDataRepository = examMetaDataRepository;
        setCrudRepository(examMetaDataRepository);
    }

    public List<ExamMetaData> getExamMetaData() {
        return examMetaDataRepository.findAll();
    }

    public ExamMetaData getExamMetaDataBySubCat(String subCat) {
        return examMetaDataRepository.findByExamSubCategory(subCat);
    }
}
