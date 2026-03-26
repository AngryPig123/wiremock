package com.wiremock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class WiremockApplication {

    public static void main(String[] args) {
        SpringApplication.run(WiremockApplication.class, args);
    }

}
