package com.examsofbharat.bramhsastra.agni.controller;

import com.examsofbharat.bramhsastra.akash.service.ClientService;
import com.examsofbharat.bramhsastra.akash.service.ResponseManagementService;
import com.examsofbharat.bramhsastra.akash.service.SecondaryPageService;
import com.examsofbharat.bramhsastra.jal.dto.request.ApplicationRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EligibilityCheckRequestDTO;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/form")
@CrossOrigin("*")
public class FormController {

    @Autowired
    ResponseManagementService responseManagementService;

//    @Autowired
//    ApplicationClientService applicationClientService;

    @Autowired
    ClientService clientService;

    @Autowired
    SecondaryPageService secondaryPageService;

    @PostMapping("/update/client/home/page")
    public Response updateHomePage(){
        log.info("Request reached for update landing page");
        return responseManagementService.buildAndUpdateClientHomePage();
    }

    @PostMapping("/fetch/client/home/response")
    public Response fetchClientHomeResponse(@RequestParam String responseType){
        log.info("Request reached for home page responseType :: {}", responseType);
        return responseManagementService.fetchHomeResponse(responseType);
    }

    @PostMapping("/fetch/form/details")
    public Response fetchFormDetails(@RequestBody ApplicationRequestDTO applicationRequestDTO){
        log.info("Request reached for form details userId :: {}", applicationRequestDTO.getUserId());
        return clientService.buildAndGetApplication(applicationRequestDTO.getUserId());
    }

//    @PostMapping("/fetch/second/page")
//    public Response fetchSecondPage(@RequestParam String requestType){
//        log.info("Fetch second page request reached ::{}" ,requestType);
//        return secondaryPageService.fetchSecondPageData(requestType);
//    }


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
}
