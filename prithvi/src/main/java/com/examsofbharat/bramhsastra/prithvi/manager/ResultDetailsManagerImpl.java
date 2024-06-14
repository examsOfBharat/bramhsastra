package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ResultDetailsRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.AdmitCard;
import com.examsofbharat.bramhsastra.prithvi.entity.ResultDetails;
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
public class ResultDetailsManagerImpl extends GenericManager<ResultDetails, String> {

    ResultDetailsRepository resultDetailsRepository;

    @Autowired
    public ResultDetailsManagerImpl(ResultDetailsRepository resultDetailsRepository){
        this.resultDetailsRepository = resultDetailsRepository;
        setCrudRepository(resultDetailsRepository);
    }


    public List<ResultDetails> getLatestResultList(int page, int size, String dateType) {
        // Assuming AdmitCardRepository has method to fetch latest admit cards
        // sorted by creation date
        Pageable pageable = PageRequest.of(page, size, Sort.by(dateType).descending());
        Page<ResultDetails> pageResult = resultDetailsRepository.findAll(pageable);
        return pageResult.getContent();
    }
}
