package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.UpcomingFormsRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.GenericResponseV1;
import com.examsofbharat.bramhsastra.prithvi.entity.UpcomingForms;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpcomingFormsManagerImpl extends GenericManager<UpcomingForms, String> {

    UpcomingFormsRepository upcomingFormsRepository;

    @Autowired
    public  UpcomingFormsManagerImpl(UpcomingFormsRepository upcomingFormsRepository){
        this.upcomingFormsRepository = upcomingFormsRepository;
        setCrudRepository(upcomingFormsRepository);
    }

    public List<UpcomingForms> getLatestUpcomingForm(int page, int size, String dateType) {

        // sorted by creation date
        Pageable pageable = PageRequest.of(page, size, Sort.by(dateType).descending());
        Page<UpcomingForms> pageResult = upcomingFormsRepository.findAll(pageable);
        return pageResult.getContent();
    }
}
