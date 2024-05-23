package com.examsofbharat.bramhsastra.akash.service;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FormService {

    public Response formLandingResponse(){
        return Response.ok().build();
    }

}
