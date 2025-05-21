package com.admitgenius.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; 

@SpringBootApplication
@ComponentScan("com.admitgenius.backend")
public class AdmitGeniusBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdmitGeniusBackEndApplication.class, args);
    }
} 


















