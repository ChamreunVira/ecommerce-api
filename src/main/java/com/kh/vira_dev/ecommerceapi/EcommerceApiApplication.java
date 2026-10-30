package com.kh.vira_dev.ecommerceapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;


@Slf4j
@SpringBootApplication
public class EcommerceApiApplication {

    public static void main(String[] args) {
       SpringApplication application = new SpringApplication(EcommerceApiApplication.class);
       application.setDefaultProperties(Map.of(
               "MAIL_HOST", "localhost",
               "MAIL_PORT", "25",
               "MAIL_USERNAME", "",
               "MAIL_PASSWORD", ""
       ));
       application.run(args);
    }

}
