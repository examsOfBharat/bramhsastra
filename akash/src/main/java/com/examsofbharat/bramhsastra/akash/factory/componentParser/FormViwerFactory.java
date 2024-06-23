package com.examsofbharat.bramhsastra.akash.factory.componentParser;

import com.examsofbharat.bramhsastra.akash.processors.*;
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
    @Autowired
    private ImportantButtonDetailParser importantButtonDetailParser;

    @PostConstruct
    public void init(){
        if(contentParserHashMap == null)
            contentParserHashMap = new HashMap<>();
        contentParserHashMap.put(ComponentEnum.APP_INTRO, applicationIntroParser);
        contentParserHashMap.put(ComponentEnum.AGE_FEE_DETAILS, timeAndFeeSummaryParser);
        contentParserHashMap.put(ComponentEnum.VACANCY_ELIGIBILITY_DETAILS, vacancyEligibilitySummaryParser);
        contentParserHashMap.put(ComponentEnum.CONTENT_DETAILS, applicationContentParser);
        contentParserHashMap.put(ComponentEnum.IMPORTANT_BUTTONS, importantButtonDetailParser);
    }

    public ContentParser get(ComponentEnum componentEnum){
        return contentParserHashMap.get(componentEnum);
    }
}
