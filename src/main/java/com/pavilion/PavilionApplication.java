package com.pavilion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PavilionApplication {
    public static void main(String[] args) {
        new SpringApplication(PavilionApplication.class).run(args);
    }
}
