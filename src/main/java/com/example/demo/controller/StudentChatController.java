package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.AdminModel;
import com.example.demo.model.StudentModel;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.FacultyRepository;
import com.example.demo.repo.StudentRepository;
import com.example.demo.service.EmailService;

@Controller
public class StudentChatController {
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
	
	@GetMapping("/StudentsChat")
	 public String FacultyChat(Model model,
			 @RequestParam("email") String email
			 
			 ) {
	     StudentModel students = studentRepo.findByEmail(email);
	     model.addAttribute("students", students);

	     return "StudentsChat";  
	 }
}
