package com.examsofbharat.bramhsastra.agni;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.examsofbharat.bramhsastra"})
@EnableScheduling
public class AgniApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgniApplication.class, args);
    }


    @Bean
    public ResourceConfig resourceConfig() {
        ResourceConfig config = new ResourceConfig();
        config.packages("com.examsofbharat.bramhsastra.akash"); // Adjust to your package
        return config;
    }

}
