package com.examsofbharat.bramhsastra.akash.facade;

import com.examsofbharat.bramhsastra.akash.service.ClientService;
import com.examsofbharat.bramhsastra.akash.utils.WebUtils;
import com.examsofbharat.bramhsastra.jal.constants.WebConstants;
import com.examsofbharat.bramhsastra.jal.dto.response.AdmitCardResponseDTO;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.examsofbharat.bramhsastra.jal.constants.ErrorConstants.DATA_NOT_FOUND;

@Slf4j
@Component
public class ApplicationClientFacade {
    @Autowired
    DBMgmtFacade dbMgmtFacade;

    @Autowired
    ClientService clientService;

    @Autowired
    WebUtils webUtils;

    public Response buildAndGetAdmitCard(String admitCardId){
        if(StringUtil.isEmpty(admitCardId)){
            return webUtils.invalidRequest();
        }
        AdmitCardResponseDTO admitCardResponseDTO = new AdmitCardResponseDTO();

        clientService.buildAdmitCardResponse(admitCardResponseDTO, admitCardId);

        if(Objects.nonNull(admitCardResponseDTO.getAdmitCardIntroDTO())){
            return  Response.ok(admitCardResponseDTO).build();
        }
        return webUtils.buildErrorMessage(WebConstants.ERROR, DATA_NOT_FOUND);
    }
}
