package com.pavilion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class PavilionApplication {
    public static void main(String[] args) {
        new SpringApplication(PavilionApplication.class).run(args);
    }
}
