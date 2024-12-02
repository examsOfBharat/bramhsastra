package com.examsofbharat.bramhsastra.akash.service.mailService;

import com.examsofbharat.bramhsastra.akash.utils.DateUtils;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.akash.utils.PdfGeneratorUtil;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.admin.AdminGenericResponseV1;
import com.examsofbharat.bramhsastra.prithvi.entity.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailUtils {

    @Autowired
    EmailService emailService;

    public void buildResultPdfAndSendMail(AdminGenericResponseV1 resultResponse, UserDetails userDetails) {
        byte[] pdfBytes = new byte[0];
        try {
            pdfBytes = PdfGeneratorUtil.generateResultPdf(resultResponse, userDetails);
        } catch (Exception e) {
            log.info("Exception occurred while generating admit pdf");
        }
        log.info("PDF generated");

        String to = "bibhu.bhushan0403@gmail.com";
        String subject = "ExamsOfBharat admin form submitted";
        String body = FormUtil.buildEmailHtml("Approver", userDetails.getFirstName(),
                resultResponse.getTitle());
        String attachmentName = DateUtils.getDateFileName("result", "pdf");
        emailService.sendEmailWithPDFAttachment(to, subject, body, pdfBytes, attachmentName);
    }

    public void buildAdmitPdfAndSendMail(AdminGenericResponseV1 admitCardRequestDTO, UserDetails userDetails) {
        byte[] pdfBytes = new byte[0];
        try {
            pdfBytes = PdfGeneratorUtil.generatePdf(admitCardRequestDTO);
        } catch (Exception e) {
            log.info("Exception occurred while generating admit pdf");
        }
        log.info("PDF generated");

        String to = "bibhu.bhushan0403@gmail.com";
        String subject = "ExamsOfBharat admin form submitted";
        String body = FormUtil.buildEmailHtml("Approver", userDetails.getFirstName(),
                admitCardRequestDTO.getTitle());
        String attachmentName = DateUtils.getDateFileName("admit", "pdf");
        emailService.sendEmailWithPDFAttachment(to, subject, body, pdfBytes, attachmentName);
    }

    public void buildFormPdfAndSendMail(EnrichedFormDetailsDTO enrichedFormDetailsDTO, UserDetails userDetails) {
        byte[] pdfBytes = PdfGeneratorUtil.generateFormPdfDoc(enrichedFormDetailsDTO);
        log.info("PDF generated");

        String to = "bibhu.bhushan0403@gmail.com";
        String subject = enrichedFormDetailsDTO.getApplicationFormDTO().getExamName();
        String body = FormUtil.buildEmailHtml("Approver", userDetails.getFirstName(),
                enrichedFormDetailsDTO.getApplicationFormDTO().getExamName());
        String attachmentName = DateUtils.getDateFileName("form", "pdf");
        emailService.sendEmailWithPDFAttachment(to, subject, body, pdfBytes, attachmentName);

        String to1 = userDetails.getEmailId();
        String subject1 = enrichedFormDetailsDTO.getApplicationFormDTO().getExamName();
        String body1 = FormUtil.buildEmailHtml("Admin", userDetails.getFirstName(),
                enrichedFormDetailsDTO.getApplicationFormDTO().getExamName());
        String attachmentName1 = DateUtils.getDateFileName("form", "pdf");
        emailService.sendEmailWithPDFAttachment(to1, subject1, body1, pdfBytes, attachmentName1);
    }

}
