package com.example.demo;

import org.hibernate.cfg.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class StudentSystemManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentSystemManagementApplication.class, args);
	}
	@PostConstruct
	public void logPort() {
	    System.out.println("Application running on port: " + System.getProperty("server.port"));
        System.out.println("Server Port: " + Environment.getProperties());

	}


}
