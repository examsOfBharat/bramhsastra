package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.LogoUrlManagerRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.LogoUrlManager;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogoUrlManagerImpl extends GenericManager<LogoUrlManager, String> {

    LogoUrlManagerRepository logoUrlManagerRepository;

    @Autowired
    public LogoUrlManagerImpl(LogoUrlManagerRepository logoUrlManagerRepository){
        this.logoUrlManagerRepository = logoUrlManagerRepository;
        setCrudRepository(logoUrlManagerRepository);
    }

    public LogoUrlManager findLogoByName(String name){
        return logoUrlManagerRepository.findByName(name);
    }

}
