package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.AdminResponseManagerRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.AdminResponseManager;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AdminResponseManagerImpl extends GenericManager<AdminResponseManager, String> {
    AdminResponseManagerRepository adminResponseManagerRepository;

    @Autowired
    public AdminResponseManagerImpl(AdminResponseManagerRepository adminResponseManagerRepository){
        this.adminResponseManagerRepository = adminResponseManagerRepository;
        setCrudRepository(adminResponseManagerRepository);
    }

    public AdminResponseManager findAdminResById(String resId){
        Optional<AdminResponseManager> adminResponseManager = adminResponseManagerRepository.findById(resId);
        return adminResponseManager.orElse(null);
    }

    public List<AdminResponseManager> fetchAdminDataByStatus(String status){
        return adminResponseManagerRepository.findAllByStatusOrderByDateCreatedDesc(status);
    }
}
