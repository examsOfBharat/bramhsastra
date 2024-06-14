package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ResultContentManagerRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ResultContentManager;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResultContentManagerImpl extends GenericManager<ResultContentManager, String> {

    ResultContentManagerRepository resultContentManagerRepository;

    @Autowired
    public ResultContentManagerImpl(ResultContentManagerRepository resultContentManagerRepository) {
        this.resultContentManagerRepository = resultContentManagerRepository;
        setCrudRepository(resultContentManagerRepository);
    }

}
