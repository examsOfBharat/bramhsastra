package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.ResultDetailsRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.ResultDetails;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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


    public Optional<List<ResultDetails>> getTopXResult(int Limit, String dateType){
        Page<ResultDetails> page = resultDetailsRepository.findAll(PageRequest.of(0,Limit,
                Sort.by(Sort.Order.desc(dateType))));
        return Optional.of(page.getContent());
    }
}
