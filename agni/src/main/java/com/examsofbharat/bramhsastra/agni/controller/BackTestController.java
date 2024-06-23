package com.examsofbharat.bramhsastra.agni.controller;

import com.examsofbharat.bramhsastra.akash.facade.ClientFacade;
import com.examsofbharat.bramhsastra.akash.facade.CredFacade;
import com.examsofbharat.bramhsastra.akash.service.*;
import com.examsofbharat.bramhsastra.jal.dto.request.*;
import com.examsofbharat.bramhsastra.prithvi.manager.ApplicationFormManagerImpl;
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
    ResponseManagementService responseManagementService;

    @Autowired
    ApplicationFormManagerImpl applicationFormManager;

    @Autowired
    FormAdminService formAdminService;

    @Autowired
    private SecondaryPageService secondaryPageService;

    @Autowired
    ClientFacade clientFacade;

    @Autowired
    ClientService clientService;


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
        log.info("Request reached for update landing page");
        return responseManagementService.buildAndUpdateClientHomePage();
    }

    @PostMapping("/get/response")
    public Response getLandingResponse(@RequestParam String responseType){
        log.info("Request reached for home page responseType :: {}", responseType);
        return responseManagementService.fetchHomeResponse(responseType);
    }

    //save details from frontend

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


    @GetMapping("/get/form/details")
    public Response getFormDetails(@RequestParam String appId){
        log.info("Request reached for form details userId :: {}", appId);
        return clientService.buildAndGetApplication(appId);
    }

    @GetMapping("/get/admit/details")
    public Response getAdmitDetails(@RequestParam String appId){
        log.info("Request reached for admit card userId :: {}", appId);
        return clientFacade.buildAndGetAdmitCard(appId);
    }

    @GetMapping("/get/result/details")
    public Response getResultDetails(@RequestParam String appId){
        log.info("Request reached for result card userId :: {}", appId);
        return clientFacade.buildAndGetResult(appId);
    }

    @GetMapping("/get/form/admit/details")
    public Response fetchAdmitDetailsByAppId(@RequestParam String appId){
        log.info("Request reached for admit by appId ::{}", appId);
        return clientFacade.fetchAdmitByAppId(appId);
    }


    @PostMapping("/fetch/name")
    public Response fetchName(){
        log.info("Fetch name request reached ::");
        return formAdminService.fetchAllAppName();
    }


//    @PostMapping("/fetch/second/page")
//    public Response fetchSecondPage(@RequestParam String requestType){
//        log.info("Fetch second page request reached ::{}" ,requestType);
//        return secondaryPageService.fetchSecondPageData(requestType);
//    }

    @GetMapping("fetch/by/min")
    public Response fetchByMin(@RequestParam String min){
        log.info("Min request reached");
        return Response.ok(applicationFormManager.findByMinQual(min)).build();
    }

    @PostMapping("/check/eligibility")
    public Response checkEligibility(@RequestBody EligibilityCheckRequestDTO eligibilityCheckRequestDTO){
        log.info("Request for eligibility check reached ::{}", eligibilityCheckRequestDTO.toString());
        return clientService.checkEligibility(eligibilityCheckRequestDTO);
    }

    @GetMapping("/fetch/page/list")
    public Response fetchSecondaryPageData(@RequestParam String subType, @RequestParam int page, @RequestParam int size){
        log.info("Request reached for 2nd page List: subtype::{} page::{} size::{}", subType, page, size);
        return secondaryPageService.fetchSecondaryPageDataV2(subType, page,size);
    }





    @GetMapping("/update/home/count")
    public Response testAPI(@RequestParam int totalVacancy){
        log.info("Request reached for update home count :: {}", totalVacancy);
        formAdminService.updateHomeCount(totalVacancy);
        return Response.ok().build();
    }

}
