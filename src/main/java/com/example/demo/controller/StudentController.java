package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.StudentModel;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.FacultyRepository;
import com.example.demo.repo.StudentRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.EmailService;
import com.example.demo.validation.adminValidation;


@Controller
public class StudentController {
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

	@GetMapping("/StudentPage")
	 public String studentPage(Model model) {
	     StudentModel students = studentRepo.findByEmail(email);
	     model.addAttribute("students", students);
	     return "StudentPage";
	 }
	 
	 @GetMapping("/StudentProfile")
	 public String FacultyProfile(@RequestParam("email") String email, Model model) {
	     StudentModel students = studentRepo.findByEmail(email);
	     System.out.println(email);
	     model.addAttribute("students", students);
	     return "StudentProfile";
	 }
	 
	 
	 @GetMapping("/payFees")
	 public String payFees(@RequestParam("email") String email, Model model) {
	     StudentModel students = studentRepo.findByEmail(email);
	     System.out.println(email);
	     model.addAttribute("students", students);
	     return "payFees";
	 }


}
