package com.examsofbharat.bramhsastra.akash.facade;


import com.examsofbharat.bramhsastra.akash.service.CredService;
import com.examsofbharat.bramhsastra.akash.utils.WebUtils;
import com.examsofbharat.bramhsastra.jal.dto.request.LogInDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.RegisterDTO;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.google.gson.GsonBuilder;

import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class CredFacade {

    @Autowired
    WebUtils webUtils;

    @Autowired
    CredService credService;

    public Response signUp(String registerData) {
        if(StringUtil.isEmpty(registerData)){
            return webUtils.invalidRequest();
        }

        RegisterDTO registerDTO = new GsonBuilder().serializeNulls().create().fromJson(registerData, RegisterDTO.class);
        return credService.doUserSignUp(registerDTO);
    }

    public Response login(String loginData){
        if(StringUtil.isEmpty(loginData)){
            return webUtils.invalidRequest();
        }

        LogInDTO logInDTO = new GsonBuilder().serializeNulls().create().fromJson(loginData, LogInDTO.class);
        return credService.doUserLogIn(logInDTO);
    }


    public Response verifyLoginOtp(String loginData){
        if(StringUtil.isEmpty(loginData)){
            return webUtils.invalidRequest();
        }

        LogInDTO loginDTO = new GsonBuilder().serializeNulls().create().fromJson(loginData, LogInDTO.class);
        return credService.verifyOtp(loginDTO);
    }
}
