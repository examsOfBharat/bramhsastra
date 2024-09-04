package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.GenericContentManagerRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.GenericContentManager;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericContentManagerImpl extends GenericManager<GenericContentManager, String> {

    GenericContentManagerRepository genericContentManagerRepository;

    @Autowired
    public GenericContentManagerImpl(GenericContentManagerRepository genericContentManagerRepository){
        this.genericContentManagerRepository = genericContentManagerRepository;
        setCrudRepository(genericContentManagerRepository);
    }

    public GenericContentManager fetchGenericContentV1(String formId){
        return genericContentManagerRepository.findByFormIdRef(formId);
    }
}
