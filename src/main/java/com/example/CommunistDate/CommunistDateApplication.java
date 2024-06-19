package com.example.CommunistDate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableWebMvc 
@ComponentScan(basePackages = {"com.example.CommunistDate"})
public class CommunistDateApplication extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(CommunistDateApplication.class);

    public static void main(String[] args) {
        
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "8080";
        }
        
        System.setProperty("server.port", port);
        logger.info("Here we are, application starting...");
        SpringApplication.run(CommunistDateApplication.class, args);
        logger.info("Finally, application started...");
    }
}