package com.ms.adproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class AdprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdprojectApplication.class, args);
    }

    public String hello() {
        return "hello";
    }
}
