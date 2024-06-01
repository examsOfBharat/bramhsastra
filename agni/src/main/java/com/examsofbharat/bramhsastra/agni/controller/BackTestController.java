package com.examsofbharat.bramhsastra.agni.controller;

import com.examsofbharat.bramhsastra.akash.facade.CredFacade;
import com.examsofbharat.bramhsastra.akash.service.ApplicationClientService;
import com.examsofbharat.bramhsastra.akash.service.CredService;
import com.examsofbharat.bramhsastra.akash.service.HomePageService;
import com.examsofbharat.bramhsastra.jal.dto.request.ApplicationRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.OwnerLandingRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.LogInDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.RegisterDTO;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/user1")
@CrossOrigin("*")
public class BackTestController {

    @Autowired
    CredFacade credFacade;

    @Autowired
    CredService credService;

    @Autowired
    HomePageService homePageService;

    @Autowired
    ApplicationClientService applicationClientService;

    @PostMapping("/register")
    public Response registerUser(@RequestBody RegisterDTO registerData){
        log.info("Registration  request reached ::{}" ,registerData.toString());
        return credService.doUserSignUp(registerData);
    }

    @PostMapping("/login")
    public Response userLogin(@RequestBody LogInDTO loginData){
        log.info("Login request reached ::{}" ,loginData.toString());
        return credService.doUserLogIn(loginData);
    }

    @PostMapping("/verify/otp")
    public Response verifyOTP(@RequestBody LogInDTO logInDTO){
        log.info("Verify OTP request reached ::{}" ,logInDTO.toString());
        return credService.verifyOtp(logInDTO);
    }

    @PostMapping("/get/owner/landing/details")
    public Response getOwnerLandingDetails(@RequestBody OwnerLandingRequestDTO ownerLandingRequestDTO){
        log.info("get owner landing request reached ::{}" ,ownerLandingRequestDTO.toString());
        return credService.getUserDetails(ownerLandingRequestDTO);
    }
    @PostMapping("/update/user/status")
    public Response updateUserStatus(@RequestBody OwnerLandingRequestDTO ownerLandingRequestDTO){
        log.info("Update user status request reached ::{}" ,ownerLandingRequestDTO.toString());
        return credService.updateUserStatus(ownerLandingRequestDTO);
    }

    @PostMapping("/update/landing/page")
    public Response updateLandingPage(){
        log.info("Request reached for landing page");
        return homePageService.buildLandingPageDto();
    }

    @PostMapping("/get/response")
    public Response getLandingResponse(@RequestParam String responseType){
        log.info("Request reached for home page responseType :: {}", responseType);
        return homePageService.fetchHomeResponse(responseType);
    }

//    @PostMapping("/save/form/detail")
//    public Response saveFormDetail(@RequestBody FormDetailsDTO formDetailsDTO){
//        log.info("Save form detail request reached ::{}" ,formDetailsDTO.toString());
//        return
//
//    }

    @PostMapping("/get/form/details")
    public Response getFormDetails(@RequestBody ApplicationRequestDTO applicationRequestDTO){
        log.info("Request reached for form details userId :: {}", applicationRequestDTO.getUserId());
        return applicationClientService.buildAndGetApplication(applicationRequestDTO.getUserId());
    }



}
