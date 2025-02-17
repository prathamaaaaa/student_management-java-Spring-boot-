package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.AdminModel;
import com.example.demo.model.StudentModel;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.FacultyRepository;
import com.example.demo.repo.StudentRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.EmailService;
import com.example.demo.validation.adminValidation;


@Controller
public class LoginController {
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


 @GetMapping("/user")
 public String userPage(Model model) {
     return "user";  
 }

 @GetMapping("/login")
 public String login(Model adminModel) {
 	
 	System.out.println("loginr");
 	return "login";
		
 }
 @PostMapping("/login/check")
 public String loginCheck(    	
 	@RequestParam String email,
 	@RequestParam String password,
 	Model model	) {
 	
 	AdminModel admin = adminRepo.findByEmail(email); 
 	if (admin!=null && admin.getPassword().equals(password)) {
 		
         model.addAttribute("admins", admin);
         
         if(admin.getRole().equalsIgnoreCase("FACULTY"))
         {
         	List<StudentModel> students = studentRepo.findAll();
         	model.addAttribute("students", students);
             model.addAttribute("admins", admin);
         	StudentModel sm = new StudentModel();
         	return "facultyPage";  
         }
         if(admin.getRole().equalsIgnoreCase("USER"))
         {
         	StudentModel students = studentRepo.findByEmail(email);
         	model.addAttribute("students", students);
         	StudentModel sm = new StudentModel();
             return "StudentProfile";
         }
         
         
         return "StudentProfile";
         
     } else {
     	return "login";  
     	
     }	
 }
}
