package com.examsofbharat.bramhsastra.akash.utils;

import com.examsofbharat.bramhsastra.jal.constants.ErrorConstants;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
import com.examsofbharat.bramhsastra.jal.dto.response.WebResponse;
import com.examsofbharat.bramhsastra.jal.utils.ResponseUtil;
import com.google.gson.Gson;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class WebUtils {

    @Autowired
    ResponseUtil responseUtil;

    public Response invalidRequest(){
        WebResponse webResponse = new WebResponse();
        webResponse.setStatus(WebConstants.ERROR);
        webResponse.setErrorResponse(responseUtil.setErrorResponse(ErrorConstants.INVALID_REQUEST));

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
        responseBuilder.entity(new Gson().toJson(webResponse));
        return responseBuilder.build();
    }

    public Response userAlreadyExistsError(){
        WebResponse webResponse = new WebResponse();
        webResponse.setStatus(WebConstants.ERROR);
        webResponse.setErrorResponse(responseUtil.setErrorResponse(ErrorConstants.USER_ALREADY_EXISTS));

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
        responseBuilder.entity(new Gson().toJson(webResponse));
        return responseBuilder.build();
    }

    public Response buildErrorMessage(String status, String errorType){
        try {
            WebResponse webResponse = new WebResponse();
            webResponse.setStatus(status);
            webResponse.setErrorResponse(responseUtil.setErrorResponse(errorType));
            Response.ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
            responseBuilder.entity(new Gson().toJson(webResponse));
            return responseBuilder.build();
        }catch (Exception e){
            log.error("Invalid request error", e);
        }
        return null;
    }

    public Response buildSuccessResponse(String status){
        WebResponse webResponse = new WebResponse();
        webResponse.setStatus(status);
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        responseBuilder.entity(new Gson().toJson(webResponse));
        return responseBuilder.build();
    }

    public Response buildSuccessResponseWithData(String status, String userId, String userType){
        WebResponse webResponse = new WebResponse();
        webResponse.setStatus(status);
        webResponse.setUserId(userId);
        webResponse.setUserRole(userType);
        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        responseBuilder.entity(new Gson().toJson(webResponse));
        return responseBuilder.build();
    }


}
