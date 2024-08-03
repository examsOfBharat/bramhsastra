package com.examsofbharat.bramhsastra.akash.service.clientService;

import com.examsofbharat.bramhsastra.akash.utils.ApplicationDbUtil;
import com.examsofbharat.bramhsastra.akash.utils.EobInitilizer;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.akash.utils.WebUtils;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
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
    EobInitilizer eobInitilizer;

    @Autowired
    private ApplicationDbUtil applicationDbUtil;


    public Response fetchSecondaryPageDataV2(String subType, int pageNo, int size){
        size = eobInitilizer.getSecPageItemCount();

        if( StringUtil.isEmpty(subType)
                || pageNo < 0 || size <= 0){
            return webUtils.invalidRequest();
        }

        String responseData = null;
        if(pageNo < 3){
            responseData = FormUtil.cacheData.get(pageNo + "_" + subType);
            if(StringUtil.notEmpty(responseData)){
                log.info("Delivering second page from cache ");
                return Response.ok(responseData).build();
            }
        }

        if(StringUtil.isEmpty(responseData) && pageNo == 0){
            ResponseManagement responseManagement = dbMgmtFacade.getResponseData(subType);
            if(Objects.nonNull(responseManagement) && StringUtil.notEmpty(responseManagement.getResponse())){
                responseData = responseManagement.getResponse();
            }
        }

        if(StringUtil.notEmpty(responseData)){
            return Response.ok(responseData).build();
        }

        String response = applicationDbUtil.fetchSecDataAndRelatedData(subType, pageNo,size, null);
        if(StringUtil.isEmpty(response)){
            return webUtils.buildErrorMessage(WebConstants.ERROR, DATA_NOT_FOUND);
        }
        return Response.ok(response).build();
    }



}
