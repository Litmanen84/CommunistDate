package com.example.CommunistDate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.example.CommunistDate"})
public class CommunistDateApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        // Specifica la porta Heroku dalla variabile d'ambiente $PORT
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "8080"; // Porta predefinita nel caso la variabile d'ambiente non sia disponibile
        }
        
        // Configura il server web Spring Boot per ascoltare sulla porta fornita
        System.setProperty("server.port", port);

        // Avvia l'applicazione Spring Boot
        SpringApplication.run(CommunistDateApplication.class, args);
    }
}

