package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.AdmitCardRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.AdmitCard;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AdmitCardManagerImpl extends GenericManager<AdmitCard, String> {

    AdmitCardRepository admitCardRepository;

    @Autowired
    public AdmitCardManagerImpl(AdmitCardRepository admitCardRepository){
        this.admitCardRepository = admitCardRepository;
        setCrudRepository(admitCardRepository);
    }

    public Optional<List<AdmitCard>> getTopXAdmitCard(int Limit, String dateType){
        Page<AdmitCard> page = admitCardRepository.findAll(PageRequest.of(0,Limit,
                Sort.by(Sort.Order.desc(dateType))));
        return Optional.of(page.getContent());
    }
}
