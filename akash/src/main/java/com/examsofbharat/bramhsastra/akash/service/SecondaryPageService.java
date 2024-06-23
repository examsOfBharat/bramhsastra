package com.examsofbharat.bramhsastra.akash.service;

import com.examsofbharat.bramhsastra.akash.utils.ApplicationDbUtil;
import com.examsofbharat.bramhsastra.akash.utils.WebUtils;
import com.examsofbharat.bramhsastra.akash.validator.FormValidator;
import com.examsofbharat.bramhsastra.jal.constants.ErrorConstants;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
import com.examsofbharat.bramhsastra.jal.dto.request.SecondaryPageRequestDTO;
import com.examsofbharat.bramhsastra.jal.enums.FormSubTypeEnum;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.entity.ResponseManagement;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.examsofbharat.bramhsastra.jal.constants.ErrorConstants.DATA_NOT_FOUND;

@Service
@Slf4j
public class SecondaryPageService {

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    @Autowired
    WebUtils webUtils;

    @Autowired
    private ApplicationDbUtil applicationDbUtil;


    public Response fetchSecondaryPageDataV2(String subType, int pageNo, int size){
        if( StringUtil.isEmpty(subType)
                || pageNo < 0 || size <= 0){
            return webUtils.invalidRequest();
        }

        if(pageNo == 0){
            ResponseManagement responseManagement = dbMgmtFacade.getResponseData(subType);
            if(Objects.nonNull(responseManagement) && StringUtil.notEmpty(responseManagement.getResponse())){
                return Response.ok(responseManagement.getResponse()).build();
            }
        }

        String response = applicationDbUtil.fetchResponseBasedOnSubType(subType, pageNo,size, null);
        if(StringUtil.isEmpty(response)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, DATA_NOT_FOUND);
        }
        return Response.ok(response).build();
    }

    // Fetch initial 2nd page data.
    // Retry if initial page is not available in response data.
    // Data not found on re-try, return not found
//    public Response fetchSecondPageData(String requestType){
//        if(StringUtil.isEmpty(requestType)){
//            return webUtils.invalidRequest();
//        }
//
//        try {
//            ResponseManagement responseManagement = dbMgmtFacade.getResponseData(requestType);
//            String response = null;
//            if (Objects.nonNull(responseManagement)) {
//                response = applicationDbUtil.fetchResponseBasedOnSubType(FormSubTypeEnum.valueOf(requestType), 0,10);
//            }
//
//            if (StringUtil.isEmpty(response)) {
//                return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.DATA_NOT_FOUND);
//            }
//            return Response.ok(response).build();
//        }catch (Exception e){
//            log.error("Exception occurred while fetching 2nd page requestType::{}",requestType, e);
//        }
//        return webUtils.buildErrorMessage(WebConstants.ERROR, ErrorConstants.DATA_NOT_FOUND);
//    }
}
