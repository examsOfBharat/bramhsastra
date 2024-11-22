package com.examsofbharat.bramhsastra.akash.facade;

import com.examsofbharat.bramhsastra.akash.executor.FormExecutorService;
import com.examsofbharat.bramhsastra.akash.service.clientService.ClientService;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.akash.utils.WebUtils;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
import com.examsofbharat.bramhsastra.jal.dto.response.AdmitCardResponseDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.AnsKeyResponseDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.ResultResponseDTO;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import com.google.gson.Gson;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.examsofbharat.bramhsastra.jal.constants.ErrorConstants.DATA_NOT_FOUND;

@Slf4j
@Component
public class ClientFacade {
    @Autowired
    DBMgmtFacade dbMgmtFacade;

    @Autowired
    ClientService clientService;

    @Autowired
    WebUtils webUtils;

    public Response saveEventResponse(String event){
        if(StringUtil.isEmpty(event)){
            return webUtils.invalidRequest();
        }

        //save source async
        FormExecutorService.mailExecutorService.submit(()->
                clientService.saveApiRequestLog("organic", null, event));

        return webUtils.buildSuccessResponse("SUCCESS");
    }

    /**
     * Fetch application form details from cache
     * if data not present in cache then call service layer for fresh fetch
     * save utm data async into database
     * @param appId
     * @param utmSource
     * @param pageType
     * @return
     */
    public Response buildAndGetApplicationForm(String appId, String utmSource, String pageType){

        if(StringUtil.isEmpty(appId)){
            return webUtils.invalidRequest();
        }

        //save source async
        FormExecutorService.mailExecutorService.submit(()->
                clientService.saveApiRequestLog(utmSource, appId, pageType));

        String  formResponse = FormUtil.formCache.get(appId);

        if(StringUtil.notEmpty(formResponse)){
            log.info("Form returned from cache id::{}", appId);
            return Response.ok(formResponse).build();
        }

        //if data is not present in cache
        return clientService.buildAndGetApplication(appId);
    }


    public Response buildAndGetAdmitCard(String admitCardId, String utmSource, String pageType){
        if(StringUtil.isEmpty(admitCardId)){
            return webUtils.invalidRequest();
        }

        //save source async
        FormExecutorService.mailExecutorService.submit(()->
                clientService.saveApiRequestLog(utmSource, admitCardId, pageType));

        AdmitCardResponseDTO admitCardResponseDTO = new AdmitCardResponseDTO();

        clientService.buildAdmitCardResponse(admitCardResponseDTO, admitCardId);

        String response = new Gson().toJson(admitCardResponseDTO);

        if(Objects.nonNull(admitCardResponseDTO.getAdmitCardIntroDTO())){
            return  Response.ok(response).build();
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, DATA_NOT_FOUND);
    }

    public Response buildAndGetResult(String resultId, String utmSource, String pageType){

        if(StringUtil.isEmpty(resultId)){
            return webUtils.invalidRequest();
        }

        //save source async
        FormExecutorService.mailExecutorService.submit(()->
                clientService.saveApiRequestLog(utmSource, resultId, pageType));

        ResultResponseDTO resultResponseDTO = new ResultResponseDTO();

        clientService.buildResultResponse(resultResponseDTO, resultId);

        String response = new Gson().toJson(resultResponseDTO);

        if(Objects.nonNull(resultResponseDTO.getResultIntroDTO())){
            return  Response.ok(response).build();
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, DATA_NOT_FOUND);
    }


    /**
     * Build ans key third page response
     * @param ansKeyId
     * @param utmSource
     * @param pageType
     * @return
     */
    public Response buildAndGetAnsKeyData(String ansKeyId, String utmSource, String pageType){

        if(StringUtil.isEmpty(ansKeyId)){
            return webUtils.invalidRequest();
        }

        //save source async
        FormExecutorService.mailExecutorService.submit(()->
                clientService.saveApiRequestLog(utmSource, ansKeyId, pageType));

        AnsKeyResponseDTO ansKeyResponseDTO = new AnsKeyResponseDTO();

        clientService.buildAnsKeyResponseP3(ansKeyResponseDTO, ansKeyId);

        //convert to string
        String response = new Gson().toJson(ansKeyResponseDTO);

        if(Objects.nonNull(ansKeyResponseDTO.getAnsKeyIntroDTO())){
            return  Response.ok(response).build();
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, DATA_NOT_FOUND);
    }


    public Response fetchAdmitByAppId(String appId){
        if(StringUtil.isEmpty(appId)){
            return webUtils.invalidRequest();
        }

        AdmitCardResponseDTO admitCardResponseDTO = new AdmitCardResponseDTO();

        clientService.buildAdmitCardResponseByAppId(admitCardResponseDTO, appId);

        if(Objects.nonNull(admitCardResponseDTO.getAdmitCardIntroDTO())){
            return  Response.ok(admitCardResponseDTO).build();
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, DATA_NOT_FOUND);

    }
}
