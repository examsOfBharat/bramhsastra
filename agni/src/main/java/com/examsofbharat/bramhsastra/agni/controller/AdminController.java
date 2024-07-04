package com.examsofbharat.bramhsastra.agni.controller;

import com.examsofbharat.bramhsastra.akash.facade.CredFacade;
import com.examsofbharat.bramhsastra.akash.service.CredService;
import com.examsofbharat.bramhsastra.akash.service.FormAdminService;
import com.examsofbharat.bramhsastra.jal.dto.request.*;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    CredFacade credFacade;

    @Autowired
    CredService credService;

    @Autowired
    FormAdminService formAdminService;

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

    @PostMapping("/save/form/detail")
    public Response saveFormDetail(@RequestBody EnrichedFormDetailsDTO formDetailsDTO){
        log.info("Save form detail request reached ::{}" ,formDetailsDTO.toString());
        return formAdminService.saveForm(formDetailsDTO);
    }

    @PostMapping("/save/admit/card")
    public Response saveAdmitCard(@RequestBody AdmitCardRequestDTO admitCardRequestDTO){
        log.info("Save admit card detail request reached ::{}" ,admitCardRequestDTO.toString());
        return formAdminService.saveAdmitCard(admitCardRequestDTO);
    }

    @PostMapping("/save/result")
    public Response saveAdmitCard(@RequestBody ResultRequestDTO resultRequestDTO){
        log.info("Save result detail request reached ::{}" ,resultRequestDTO.toString());
        return formAdminService.buildAndSaveResultData(resultRequestDTO);
    }

    @PostMapping("/fetch/name")
    public Response fetchName(){
        log.info("Fetch name request reached ::");
        return formAdminService.fetchAllAppName();
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


}