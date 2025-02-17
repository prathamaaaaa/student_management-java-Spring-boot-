package com.example.demo.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.AdminModel;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.FacultyRepository;
import com.example.demo.repo.StudentRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.EmailService;
import com.example.demo.validation.adminValidation;

@Controller
public class MailController {
	
		@Autowired
	    private EmailService emailService;
	 

	    @Autowired
	    private JavaMailSender javaMailSender;

	    @Autowired
	    private AdminRepository adminRepo;
	
	    @Autowired
	    private StudentRepository studentRepo;
	
	    @Autowired
	    private FacultyRepository facultyRepo;

	    @Autowired
	    private AdminService adminService;

		@Autowired
		private adminValidation adminValidation;


		public String photoUrl;

		public int fees;

		public String email;
	
		public int totalMarks;

		public int percentage;


		private int avg;

	@PostMapping("/forgot")
	 public String forgot( Model model,
			 @RequestParam String email
			 ) {
		
		AdminModel admin = adminRepo.findByEmail(email);
		
		String generatedPassword1 =  generatePassword();
		 admin.setPassword(generatedPassword1);
		 adminRepo.save(admin);
	     

	     String emailSubject = "Your updeted Portal Password";
	        String emailText = "Hello " + admin.getName() + ",\n\nYour password for the student portal is: " + generatedPassword1;
	        emailService.sendEmail(email, emailSubject, emailText);
		
	     return "Password";
	 }
	
	  public String generatePassword() {
	        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
	        Random random = new Random();
	        StringBuilder password = new StringBuilder(8);

	        for (int i = 0; i < 8; i++) {
	            password.append(characters.charAt(random.nextInt(characters.length())));
	        }
	        return password.toString();
	    }
	    
}
