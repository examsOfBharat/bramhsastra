package com.examsofbharat.bramhsastra.agni.controller;

import com.examsofbharat.bramhsastra.akash.facade.CredFacade;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/user")
public class WebLoginController {

    @Autowired
    CredFacade credFacade;

    @PostMapping("/register")
    public Response registerUser(@RequestBody String registerData){
        return credFacade.signUp(registerData);
    }

    @PostMapping("/login")
    public Response userLogin(@RequestBody String loginData){
        return credFacade.login(loginData);
    }

    @PostMapping("/verify/otp")
    public Response verifyOTP(@RequestBody String otpData){
        return credFacade.verifyLoginOtp(otpData);
    }
}
