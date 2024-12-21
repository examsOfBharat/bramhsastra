package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.GenericResponseV1Repository;
import com.examsofbharat.bramhsastra.prithvi.entity.GenericResponseV1;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import com.examsofbharat.bramhsastra.prithvi.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class GenericResponseV1ManagerImpl extends GenericManager<GenericResponseV1, String> {

    GenericResponseV1Repository genericResponseV1Repository;

    @Autowired
    public GenericResponseV1ManagerImpl(GenericResponseV1Repository genericResponseV1Repository){
        this.genericResponseV1Repository = genericResponseV1Repository;
        setCrudRepository(genericResponseV1Repository);
    }

    public List<GenericResponseV1> getLatestGenericResponse(int page, int size, String dateType, String type) {
        // Assuming AdmitCardRepository has method to fetch latest admit cards
        // sorted by creation date
        Pageable pageable = PageRequest.of(page, size, Sort.by(dateType).descending());
        Page<GenericResponseV1> pageResult = genericResponseV1Repository.findByType(pageable, type);
        return pageResult.getContent();
    }

    public List<GenericResponseV1> getXDaysResponse(int daysGap, String type) {
        // Assuming AdmitCardRepository has method to fetch latest admit cards
        // sorted by creation date
        Date dateCriteria = DateUtils.addDays(new Date(), -daysGap);
        return genericResponseV1Repository.findByTypeAndDateCreatedAfterOrderByDateCreatedDesc(type, dateCriteria);
    }

    public List<GenericResponseV1> fetchResponseBasedOnDays(int days){

        Date dateCriteria  = DateUtils.addDays(new Date(), -days);
        return genericResponseV1Repository.findAllByDateCreatedAfterOrderByDateCreatedDesc(dateCriteria);
    }

    public GenericResponseV1 fetchResponseById(String admitId){
        Optional<GenericResponseV1> admitCard = genericResponseV1Repository.findById(admitId);
        return admitCard.orElse(null);
    }

    public GenericResponseV1 fetchResponseByAppId(String appId, String type){
        return genericResponseV1Repository.findByAppIdRefAndType(appId, type);
    }

}
