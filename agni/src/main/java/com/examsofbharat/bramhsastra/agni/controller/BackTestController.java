package com.examsofbharat.bramhsastra.agni.controller;

import com.examsofbharat.bramhsastra.akash.facade.ClientFacade;
import com.examsofbharat.bramhsastra.akash.facade.CredFacade;
import com.examsofbharat.bramhsastra.akash.service.adminService.CredService;
import com.examsofbharat.bramhsastra.akash.service.adminService.FormAdminService;
import com.examsofbharat.bramhsastra.akash.service.clientService.ClientService;
import com.examsofbharat.bramhsastra.akash.service.clientService.ResponseManagementService;
import com.examsofbharat.bramhsastra.akash.service.clientService.SecondaryPageService;
import com.examsofbharat.bramhsastra.akash.utils.WebUtils;
import com.examsofbharat.bramhsastra.jal.dto.request.*;
import com.examsofbharat.bramhsastra.jal.dto.request.admin.WrapperGenericAdminResponseV1DTO;
import com.examsofbharat.bramhsastra.jal.dto.response.BlogAdminResponse;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.manager.ApplicationFormManagerImpl;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;


@Slf4j
@RestController
@RequestMapping("/user1")
//@CrossOrigin("https://jarvis-psi.vercel.app/")
@CrossOrigin("*")
//@CrossOrigin(
//        origins = {"https://jarvis-psi.vercel.app"},
//        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT},
//        allowedHeaders = {"Authorization", "Content-Type"},
//        allowCredentials = "true")
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

    @Autowired
    WebUtils webUtils;

    @GetMapping("/test")
    public Response test(){
        return webUtils.buildSuccessResponse("SUCCESS");
    }

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

    @GetMapping("/update/landing/page")
    public Response updateLandingPage(){
        log.info("Request reached for update landing page");
        return responseManagementService.buildAndUpdateClientHomePage();
    }

    @GetMapping("/refresh")
    public Response getLandingResponse(@RequestParam String pageType){
        log.info("Request reached to refresh landing page");
        return responseManagementService.refreshIndividualSection(pageType);
    }

    //Refresh Home Update section
    @GetMapping("/update/home/updates")
    public Response updateHomeUpdateSection(){
        log.info("Request reached for refresh Home updates section");
        return responseManagementService.refreshUpdateSection();
    }


    /**
     * Fetch HOME page
     * @param appId @mandatory
     * @param responseType @mandatory
     * @return
     */
    @GetMapping("/get/response")
    public Response getLandingResponse(@RequestHeader(value = "App-Id", required = false) String appId,@RequestParam String responseType){
        if(StringUtil.isEmpty(appId) || !"abcd".equals(appId)){
            return Response.status(401).build();
        }
        log.info("Request reached for home page responseType :: {}", responseType);
        return responseManagementService.fetchHomeResponse(responseType);
    }

    /**
     *Save details from admin (forms, admit, result, anskey and upcoming forms)
     */
    @PostMapping("/save/form/detail")
    public Response saveFormDetail(@RequestBody EnrichedFormDetailsDTO formDetailsDTO){
        log.info("New Application Form Reached Name ::{}" ,
                formDetailsDTO.getApplicationFormDTO().getExamName());

        return formAdminService.processAndSaveAdminFormResponse(formDetailsDTO);
    }

    @PostMapping("/save/blog/detail")
    public Response saveBlogAdminResponse(@RequestBody BlogAdminResponse blogAdminResponse){
        log.info("New Blog Post Reached Name ::{}" ,blogAdminResponse.getBlogHeader().getTitle());
        return formAdminService.saveBlogAdminResponse(blogAdminResponse);
    }


    //Generic API for admit, result and anskey
    @PostMapping("/save/result")
    public Response saveResult(@RequestBody WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO){
        log.info("New Result Reached Name ::{}" ,
                wrapperGenericAdminResponseV1DTO.getAdminGenericResponseV1().getTitle());

        return formAdminService.processAndSaveGenericResV1(wrapperGenericAdminResponseV1DTO);
    }

    @PostMapping("/save/upcoming/forms")
    public Response saveUpcomingForms(@RequestBody WrapperGenericAdminResponseV1DTO wrapperGenericAdminResponseV1DTO){
        log.info("Save upcoming detail request reached ::{}" ,wrapperGenericAdminResponseV1DTO.toString());
        return formAdminService.processAndSaveUpcomingForm(wrapperGenericAdminResponseV1DTO);
    }


    @GetMapping("/get/form/details")
    public Response getFormDetails(@RequestHeader(value = "App-Id", required = false) String app_Id,
                                   @RequestHeader(value = "utm_source", required = false) String utmSource,
                                   @RequestParam String appId){

        log.info("get form details request reached source::{} formId::{}" ,utmSource,appId);
        if(StringUtil.isEmpty(app_Id) || !"abcd".equals(app_Id)){
            return Response.status(401).build();
        }
        return clientFacade.buildAndGetApplicationForm(appId, utmSource, "form");
    }

    //adding testing details
    @GetMapping("/get/admit/details")
    public Response getAdmitDetails(@RequestHeader(value = "App-Id", required = false) String app_Id,
                                    @RequestHeader(value = "utm_source", required = false) String utmSource,
                                    @RequestParam String appId){

        log.info("get admit card details request reached source::{} admitCardId::{}" ,utmSource,appId);
        if(StringUtil.isEmpty(app_Id) || !"abcd".equals(app_Id)){
            return Response.status(401).build();
        }
        return clientFacade.buildAndGetAdmitCard(appId,utmSource,"admit");
    }

    @GetMapping("/get/result/details")
    public Response getResultDetails(@RequestHeader(value = "App-Id", required = false) String app_Id,
                                     @RequestHeader(value = "utm_source", required = false) String utmSource,
                                     @RequestParam String appId){

        log.info("get result details request reached source::{} resultId::{}" ,utmSource,appId);
        if(StringUtil.isEmpty(app_Id) || !"abcd".equals(app_Id)){
            return Response.status(401).build();
        }
        return clientFacade.buildAndGetAdmitCard(appId, utmSource, "result");
    }

    /**
     * FETCH ANS KEY THIRD PAGE
     */
    @GetMapping("/get/anskey/details")
    public Response getAnsKeyDetails(@RequestHeader(value = "App-Id", required = false) String app_Id,
                                     @RequestHeader(value = "utm_source", required = false) String utmSource,
                                     @RequestParam String appId){

        log.info("get ansKey details request reached source::{} ansKeyId::{}" ,utmSource,appId);
        if(StringUtil.isEmpty(app_Id) || !"abcd".equals(app_Id)){
            return Response.status(401).build();
        }
        return clientFacade.buildAndGetAdmitCard(appId, utmSource, "anskey");
    }

    /**
     * FETCH BLOG THIRD PAGE
     * @param blogId
     * @return
     */
    @GetMapping("/get/blog")
    public Response fetchBlogResponse(@RequestHeader(value = "App-Id", required = false) String app_Id,
                                      @RequestParam String blogId){
        log.info("blog request reached blogId::{}" ,blogId);
        if(StringUtil.isEmpty(app_Id) || !"abcd".equals(app_Id)){
            return Response.status(401).build();
        }
        return clientFacade.fetchBlogData(blogId);
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


    /**
     * Fetch SECOND PAGE data
     * @param appId authenticate the api
     * @param subType required to return correct page data
     * @param page count of page
     * @param size count of size
     * @return
     */
    @GetMapping("/fetch/page/list")
    public Response fetchSecondaryPageData(@RequestHeader(value = "App-Id", required = false) String appId,
                                           @RequestParam String subType, @RequestParam int page, @RequestParam int size){

        if(StringUtil.isEmpty(appId) || !"abcd".equals(appId)){
            return Response.status(401).build();
        }
        log.info("Request reached for 2nd page List: subtype::{} page::{} size::{}", subType, page, size);
        return secondaryPageService.fetchSecondaryPageDataV2(subType, page,size);
    }


    @GetMapping("/update/home/count")
    public Response testAPI(@RequestParam int totalVacancy){
        log.info("Request reached for update home count :: {}", totalVacancy);
        formAdminService.updateHomeCount(totalVacancy);
        return Response.ok().build();
    }


    //Admin response data fetch API
    @GetMapping("/fetch/admin/submitted/detail")
    public Response fetchAdminSubmission(@RequestParam String status){
        log.info("Admin submission fetch call reached status::{}", status);
        return formAdminService.fetchAdminResponseData(status);
    }

    @PostMapping("/update/approver/response")
    public Response updateApproverResponse(@RequestBody ApproverRequestDTO approverRequestDTO){
        log.info("Reached approver request {}",approverRequestDTO.toString());
        return formAdminService.updateApproverResponse(approverRequestDTO);
    }



    @GetMapping("/save/event")
    public Response saveTrackingEvent(@RequestParam String pageType){

        log.info("Event details request reached ::{}" ,pageType);

        return clientFacade.saveEventResponse(pageType);
    }

}
