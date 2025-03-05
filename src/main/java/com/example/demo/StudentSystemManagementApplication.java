package com.example.demo;

import org.hibernate.cfg.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableFeignClients
public class StudentSystemManagementApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
                .filename("pass.env") // Use "pass.env" instead of ".env"
                .load();

        System.setProperty("GMAIL_USER", dotenv.get("GMAIL_USER"));
        System.setProperty("GMAIL_PASSWORD", dotenv.get("GMAIL_PASSWORD"));

		SpringApplication.run(StudentSystemManagementApplication.class, args);
		
	}
	@PostConstruct
	public void logPort() {
	    System.out.println("Application running on port: " + System.getProperty("server.port"));
        System.out.println("Server Port: " + Environment.getProperties());

	}


}
