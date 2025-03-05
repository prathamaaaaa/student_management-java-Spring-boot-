package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.AdminModel;
import com.example.demo.model.StudentModel;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.FacultyRepository;
import com.example.demo.repo.StudentRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.EmailService;
import com.example.demo.validation.adminValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/all")
public class UserController {
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

	
	 @GetMapping("/reportCard")
	 public String reportCard(@RequestParam("email") String email, Model model,@RequestParam String facultyEmail) throws JsonMappingException, JsonProcessingException {
	     StudentModel students = studentRepo.findByEmail(email);
	     System.out.println(email);
	     ObjectMapper objectMapper = new ObjectMapper();
	     List<Integer> marks = objectMapper.readValue(
	             students.getMarks1(), 
	             new com.fasterxml.jackson.core.type.TypeReference<List<Integer>>() {}
	     );
	     System.out.println("Marks Size: " + marks.size());

	     avg = students.getPercentage();
	     String pp = avgP(avg);
	     
	     AdminModel admins = adminRepo.findByEmail(facultyEmail);
	     System.out.println("percentage" +pp);
	     model.addAttribute("marks", marks);
	     model.addAttribute("pp", pp);
	     model.addAttribute("students", students);
	     model.addAttribute("admins",admins);
	     return "reportCard";
	 }
	 
	 
	 @GetMapping("/searchStudents")
	    public String searchStudents(@RequestParam("searchQuery") String searchQuery,
	    		@RequestParam String email,
	    		Model model) {
	        List<StudentModel> students = studentRepo.findByNameContainingIgnoreCase(searchQuery);
	        model.addAttribute("students", students);
	        
	        AdminModel admins = adminRepo.findByEmail(email);
	        model.addAttribute("admins", admins);

	        
	        return "facultyPage";
	    }
	 
	 @GetMapping("/update")
	 public String update(@RequestParam("email") String email, Model model) {
	     StudentModel students = studentRepo.findByEmail(email);
	     System.out.println("emial"+email);
	     String phot =students.getPhoto();
	     System.out.println("photot"+phot);
	     
	     students.setPhoto(phot);
	     studentRepo.save(students);
	     model.addAttribute("students", students);
	     return "update";
	 }
	 
	 private String avgP(int avg) {
	        if (avg >= 90) return "Your Grade is AA";
	        else if (avg >= 80) return "Your Grade is AB";
	        else if (avg >= 70) return "Your Grade is BB";
	        else if (avg >= 60) return "Your Grade is BC";
	        else if (avg >= 50) return "Your Grade is CC";
	        else if (avg >= 40) return "Your Grade is CD";
	        else if (avg >= 33) return "Your Grade is DD";
	        else return "Sorry you cannot clear the exam..!!";
	    }

}
