package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ApiRequestLogRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ApiRequestLog;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiRequestLogManagerImpl extends GenericManager<ApiRequestLog, String> {

    ApiRequestLogRepository apiRequestLogRepository;

    @Autowired
    public ApiRequestLogManagerImpl(ApiRequestLogRepository apiRequestLogRepository) {
        this.apiRequestLogRepository = apiRequestLogRepository;
        setCrudRepository(apiRequestLogRepository);
    }
}
