package com.examsofbharat.bramhsastra.agni.controller;

import com.examsofbharat.bramhsastra.akash.facade.CredFacade;
import com.examsofbharat.bramhsastra.akash.service.CredService;
import com.examsofbharat.bramhsastra.jal.dto.request.OwnerLandingRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.LogInDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.RegisterDTO;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/user1")
public class BackTestController {

    @Autowired
    CredFacade credFacade;

    @Autowired
    CredService credService;

    @PostMapping("/register")
    public Response registerUser(@RequestBody RegisterDTO registerData){
        return credService.doUserSignUp(registerData);
    }

    @PostMapping("/login")
    public Response userLogin(@RequestBody LogInDTO loginData){
        return credService.doUserLogIn(loginData);
    }

    @PostMapping("/verify/otp")
    public Response verifyOTP(@RequestBody LogInDTO logInDTO){
        return credService.verifyOtp(logInDTO);
    }

    @PostMapping("/get/owner/landing/details")
    public Response getOwnerLandingDetails(@RequestBody OwnerLandingRequestDTO ownerLandingRequestDTO){
        return credService.getUserDetails(ownerLandingRequestDTO);
    }

}
