package com.examsofbharat.bramhsastra.akash.service.adminService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@Component
@Slf4j
public class OtpService {
    private static final int OTP_LENGTH = 6;

    public String generateOTP() {
        StringBuilder otpBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < OTP_LENGTH; i++) {
            int digit = random.nextInt(10); // Generate a random digit between 0 and 9 (inclusive)
            otpBuilder.append(digit);
        }

        return otpBuilder.toString();
    }
}
