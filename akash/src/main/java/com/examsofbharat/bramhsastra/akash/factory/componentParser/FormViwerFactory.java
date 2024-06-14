package com.examsofbharat.bramhsastra.akash.factory.componentParser;

import com.examsofbharat.bramhsastra.akash.processors.ApplicationContentParser;
import com.examsofbharat.bramhsastra.akash.processors.ApplicationIntroParser;
import com.examsofbharat.bramhsastra.akash.processors.TimeAndFeeSummaryParser;
import com.examsofbharat.bramhsastra.akash.processors.VacancyEligibilitySummaryParser;
import com.examsofbharat.bramhsastra.jal.enums.ComponentEnum;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class FormViwerFactory {

    HashMap<ComponentEnum, ContentParser> contentParserHashMap;

    @Autowired
    ApplicationIntroParser applicationIntroParser;

    @Autowired
    ApplicationContentParser applicationContentParser;

    @Autowired
    TimeAndFeeSummaryParser timeAndFeeSummaryParser;

    @Autowired
    VacancyEligibilitySummaryParser vacancyEligibilitySummaryParser;

    @PostConstruct
    public void init(){
        if(contentParserHashMap == null)
            contentParserHashMap = new HashMap<>();
        contentParserHashMap.put(ComponentEnum.APP_INTRO, applicationIntroParser);
        contentParserHashMap.put(ComponentEnum.AGE_FEE_DETAILS, timeAndFeeSummaryParser);
        contentParserHashMap.put(ComponentEnum.VACANCY_ELIGIBILITY_DETAILS, vacancyEligibilitySummaryParser);
        contentParserHashMap.put(ComponentEnum.CONTENT_DETAILS, applicationContentParser);
    }

    public ContentParser get(ComponentEnum componentEnum){
        return contentParserHashMap.get(componentEnum);
    }
}
