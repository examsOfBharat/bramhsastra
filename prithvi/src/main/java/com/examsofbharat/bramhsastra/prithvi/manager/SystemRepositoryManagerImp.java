package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.SystemPropertyRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.SystemProperty;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service("systemPropertyManager")
public class SystemRepositoryManagerImp extends GenericManager<SystemProperty, String> {

    SystemPropertyRepository systemPropertyRepository;

    @Autowired
    public void setSystemPropertyRepository(SystemPropertyRepository systemPropertyRepository) {
        this.systemPropertyRepository = systemPropertyRepository;
        setCrudRepository(systemPropertyRepository);
    }

    @Override
    public void setCrudRepository(CrudRepository<SystemProperty, String> crudRepository) {
        this.crudRepository = systemPropertyRepository;
    }

    public SystemProperty getSystemProperty(String name){
        return systemPropertyRepository.findOneByName(name);
    }

}
