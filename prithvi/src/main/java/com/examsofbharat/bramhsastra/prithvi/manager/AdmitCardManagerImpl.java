package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.AdmitCardRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.AdmitCard;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public List<AdmitCard> getLatestAdmitCards(int page, int size, String dateType) {
        // Assuming AdmitCardRepository has method to fetch latest admit cards
        // sorted by creation date
        Pageable pageable = PageRequest.of(page, size, Sort.by(dateType).descending());
        Page<AdmitCard> pageResult = admitCardRepository.findAll(pageable);
        return pageResult.getContent();
    }
}
