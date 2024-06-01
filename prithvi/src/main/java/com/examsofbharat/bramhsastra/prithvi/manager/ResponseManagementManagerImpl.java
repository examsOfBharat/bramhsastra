package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ResponseManagementRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ResponseManagement;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResponseManagementManagerImpl extends GenericManager<ResponseManagement, String> {
    ResponseManagementRepository responseManagementRepository;

    @Autowired
    public ResponseManagementManagerImpl(ResponseManagementRepository responseManagementRepository){
        this.responseManagementRepository = responseManagementRepository;
        setCrudRepository(responseManagementRepository);
    }

    public ResponseManagement fetchResponseByType(String responseType){
        return responseManagementRepository.findByResponseTypeOrderByDateCreatedDesc(responseType);
    }
}
