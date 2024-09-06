package com.examsofbharat.bramhsastra.akash.service.mailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        try {
            message.setFrom("examsofbharat@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body); // true indicates HTML content
            javaMailSender.send(message);
        }
        catch (Exception e){
            log.info("Exception occurred while sending mail " ,e);
        }
        log.info("Mail sent successfully! ");
    }

    public void sendEmailWithPDFAttachment(String to, String subject, String body, byte[] pdfBytes, String attachmentName) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            // Use MimeMessageHelper to add attachments
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("examsofbharat@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body,true);

            // Attach the PDF file
            helper.addAttachment(attachmentName, new ByteArrayResource(pdfBytes));
            // Send email
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle exception properly in your application
        }
        log.info("Mail sent successfully!");
    }

    private String getEmailFormPageTag(){
        return buildEmailHtml("Bibhu Bhushan", "Sikku Saurav", "SSC GD 2024 Exam","https://examsofbharat-admin.vercel.app/login", "https://examsofbharat-admin.vercel.app/login");
    }


    private String buildEmailHtml(String recipientName, String submitterName, String formName, String approveLink, String rejectLink) {
        String htmlTemplate = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Email Template</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Arial', sans-serif;\n" +
                "            line-height: 1.6;\n" +
                "            background-color: #f2f2f2;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #ffffff;\n" +
                "            border: 1px solid #e0e0e0;\n" +
                "            border-radius: 5px;\n" +
                "            box-shadow: 0 0 10px rgba(0,0,0,0.1);\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #007bff;\n" +
                "            color: #ffffff;\n" +
                "            text-align: center;\n" +
                "            padding: 10px 0;\n" +
                "            border-radius: 5px 5px 0 0;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .header h2 {\n" +
                "            margin: 0;\n" +
                "            font-size: 24px;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding-bottom: 20px;\n" +
                "        }\n" +
                "        .button {\n" +
                "            align-items: center;\n" +
                "            appearance: none;\n" +
                "            background-image: radial-gradient(100% 100% at 100% 0, #5adaff 0, #5468ff 100%);\n" +
                "            border: 0;\n" +
                "            border-radius: 6px;\n" +
                "            box-shadow: rgba(45, 35, 66, .4) 0 2px 4px, rgba(45, 35, 66, .3) 0 7px 13px -3px, rgba(58, 65, 111, .5) 0 -3px 0 inset;\n" +
                "            box-sizing: border-box;\n" +
                "            color: #fff;\n" +
                "            cursor: pointer;\n" +
                "            display: flex;\n" +
                "            font-family: \"JetBrains Mono\", monospace;\n" +
                "            height: 48px;\n" +
                "            justify-content: center;\n" +
                "            line-height: 1;\n" +
                "            list-style: none;\n" +
                "            overflow: hidden;\n" +
                "            padding-left: 16px;\n" +
                "            padding-right: 16px;\n" +
                "            position: relative;\n" +
                "            text-align: left;\n" +
                "            text-decoration: none;\n" +
                "            transition: box-shadow .15s, transform .15s;\n" +
                "            user-select: none;\n" +
                "            -webkit-user-select: none;\n" +
                "            touch-action: manipulation;\n" +
                "            white-space: nowrap;\n" +
                "            will-change: box-shadow, transform;\n" +
                "            font-size: 18px;\n" +
                "        }\n" +
                "        .button:focus {\n" +
                "            box-shadow: #3c4fe0 0 0 0 1.5px inset, rgba(45, 35, 66, .4) 0 2px 4px, rgba(45, 35, 66, .3) 0 7px 13px -3px, #3c4fe0 0 -3px 0 inset;\n" +
                "        }\n" +
                "        .button:hover {\n" +
                "            box-shadow: rgba(45, 35, 66, .4) 0 4px 8px, rgba(45, 35, 66, .3) 0 7px 13px -3px, #3c4fe0 0 -3px 0 inset;\n" +
                "            transform: translateY(-2px);\n" +
                "        }\n" +
                "        .button:active {\n" +
                "            box-shadow: #3c4fe0 0 3px 7px inset;\n" +
                "            transform: translateY(2px);\n" +
                "        }\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            margin-top: 20px;\n" +
                "            color: #777777;\n" +
                "            font-size: 12px;\n" +
                "        }\n" +
                "        .buttons{\n" +
                "            display: flex ;\n" +
                "            justify-content: center;\n" +
                "            column-gap: 10px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h2>Email Title</h2>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Hey [recipientName],</p>\n" +
                "            <p>[submitterName] has submitted form <strong>[formName]</strong>. A PDF file has been attached for your review.</p>\n" +
                "            <div class=\"buttons\">\n" +
                "                <a href=\"[approveLink]\" class=\"button\">Click to Approve</a>\n" +
                "                <a href=\"[rejectLink]\" class=\"button reject\">Click to Reject</a>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>This email was sent via Your Company. Please do not reply to this email.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return htmlTemplate;
    }


}
