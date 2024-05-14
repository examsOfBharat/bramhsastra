package com.examsofbharat.bramhsastra.agni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.examsofbharat.bramhsastra"})
@EnableScheduling
public class AgniApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgniApplication.class, args);
    }

}
