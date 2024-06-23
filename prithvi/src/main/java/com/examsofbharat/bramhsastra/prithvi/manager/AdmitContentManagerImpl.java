package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.AdmitContentManagerRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.AdmitContentManager;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdmitContentManagerImpl extends GenericManager<AdmitContentManager, String> {

    AdmitContentManagerRepository admitContentManagerRepository;

    @Autowired
    public AdmitContentManagerImpl(AdmitContentManagerRepository admitContentManagerRepository) {
        this.admitContentManagerRepository = admitContentManagerRepository;
        setCrudRepository(admitContentManagerRepository);
    }

    public AdmitContentManager fetchAdmitCardContent(String admitIdRef){
        return admitContentManagerRepository.findByAdmitIdRef(admitIdRef);
    }

}
