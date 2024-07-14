package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.jal.enums.FormTypeEnum;
import com.examsofbharat.bramhsastra.prithvi.dao.ApplicationFormRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.AdmitCard;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationForm;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import com.examsofbharat.bramhsastra.prithvi.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ApplicationFormManagerImpl extends GenericManager<ApplicationForm, String> {
    ApplicationFormRepository applicationFormRepository;

    @Autowired
    public ApplicationFormManagerImpl(ApplicationFormRepository applicationFormRepository) {
        this.applicationFormRepository = applicationFormRepository;
        setCrudRepository(applicationFormRepository);
    }

    public ApplicationForm getApplicationFormById(String id) {
        return applicationFormRepository.findById(id).orElse(null);
    }

    public List<ApplicationForm> getLatestFormBasedOnFormType(int page, int size, String subType, FormTypeEnum formTypeEnum) {
        // Assuming AdmitCardRepository has method to fetch latest admit cards
        // sorted by creation date
        String dateType = "dateCreated";
        Pageable pageable = PageRequest.of(page, size, Sort.by(dateType).descending());
        Page<ApplicationForm> pageResult = fetchFormBasedOnFormTypes(formTypeEnum, subType, pageable);
        return pageResult.getContent();
    }

    public Page<ApplicationForm> fetchFormBasedOnFormTypes(FormTypeEnum formTypeEnum, String subType, Pageable pageable){
        return switch (formTypeEnum) {
            case QUALIFICATION_BASED ->
                    applicationFormRepository.findAllByMinQualification(subType, pageable);
            case SECTOR_BASED -> applicationFormRepository.findAllBySectors(subType, pageable);
            case GRADE_BASED -> applicationFormRepository.findAllByGrade(subType, pageable);
            case PROVINCIAL_BASED -> applicationFormRepository.findAllByState(subType, pageable);
            case FEMALE -> applicationFormRepository.findAllByGender(subType, pageable);
            default -> null;
        };
    }

    public ApplicationForm findByMinQual(String minQual){
        return applicationFormRepository.findByMinQualification(minQual);
    }

    public List<ApplicationForm> getLatestForm(int page, int size) {
        // find latest application form present in data
        // sorted by creation date
        String dateType = "startDate";
        Pageable pageable = PageRequest.of(page, size, Sort.by(dateType).descending());
        Page<ApplicationForm> pageResult = applicationFormRepository.findAll(pageable);
        return pageResult.getContent();
    }

    public List<ApplicationForm> getOldestForm(int page, int size) {
        // find latest application form present in data
        // sorted by creation date
        String dateType = "endDate";
        Date startDate = DateUtils.getStartOfDay(new Date());
        Date endDate = DateUtils.addDays(new Date(), 5);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dateType).ascending());
        Page<ApplicationForm> pageResult = applicationFormRepository.findByEndDateBetween(startDate, endDate, pageable);
        return pageResult.getContent();
    }

    public List<ApplicationForm> getFormWithAnsKey(int page, int size){
        String dateType = "answerDate";
        Date endDate = DateUtils.getEndOfDay(new Date());
        Date startDate = DateUtils.addDays(new Date(), -5);

        Pageable pageable = PageRequest.of(page, size, Sort.by(dateType).descending());
        Page<ApplicationForm> pageResult = applicationFormRepository.findByAnswerDateAfter(startDate, pageable);
        return pageResult.getContent();
    }
}
