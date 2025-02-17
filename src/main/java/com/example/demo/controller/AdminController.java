package com.example.demo.controller;
import java.io.IOException;


import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.AdminModel;
import com.example.demo.model.FacultyModel;
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

import aj.org.objectweb.asm.TypeReference;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;


@Controller
public class AdminController {
    
	
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
    
	@GetMapping("/Password")
	 public String Password( Model model) {
		
	     return "Password";
	 }
	
	
	
	@GetMapping("/register")
	public String adminPage(Model model) {
	    model.addAttribute("adminModel", new AdminModel()); 
	    return "register"; 
	}
	 @PostMapping("/submitForm")
	    public String submitForm(@Valid @ModelAttribute("adminModel") AdminModel adminModel,
	                             BindingResult result,
	                             @RequestParam String mobileNumber,
	                             Model model) {

	        if (result.hasErrors()) {
	            model.addAttribute("error", "There are errors in the form. Please correct them.");
	            return "register"; 
	        }

	        adminService.saveAdminData(adminModel , mobileNumber);

	        return "redirect:/login"; 
	    } 
	 
	 
		 

	
	 
	 @PostMapping("/updated")    
	 public String updated(@RequestParam("email") String email,
	         @RequestParam String name , 
	         @RequestParam String department,
	         @RequestParam(value = "photo", required = false) MultipartFile photo,  // Make photo optional
	         @RequestParam String division,
	         Model model) throws IOException {
	     
	     StudentModel students = studentRepo.findByEmail(email);
	     System.out.println(email);
	     
	     if (photo != null && !photo.isEmpty()) {
	         Path path = Paths.get("src/main/resources/static/" + photo.getOriginalFilename());
	         Files.createDirectories(path.getParent());
	         Files.write(path, photo.getBytes());
	         
	         students.setPhoto("/" + photo.getOriginalFilename());
	         System.out.println("New photo uploaded: " + photo.getOriginalFilename());
	     } else {
	         System.out.println("No new photo uploaded. Keeping the existing photo.");
	     }
	     
	     students.setName(name);
	     students.setDepartment(department);
	     students.setDivision(division);
	     
	     studentRepo.save(students);
	     model.addAttribute("students", students);
	     return "update";
	 }
 
//	 @PostMapping("/updated")
//	 public String updeted(@RequestParam String name ,   Model model ) {
//		 
//		 StudentModel s = studentRepo.findByEmail(email);
//		 s.setName(name);
//		 studentRepo.save(s);
//		System.out.println("s"+name); 
//		return  "update";
//		 
////	 }
	 
	

	 
	 
	 @GetMapping("/delete")
	 public String delete(Model model ,@RequestParam String facultyEmail,@RequestParam String email ) {
		 
		 StudentModel students = studentRepo.findByEmail(email);		 
		 studentRepo.delete(students);
		 AdminModel admins = adminRepo.findByEmail(email);
		 adminRepo.delete(admins);
		 
		 return "redirect:/facultyPage?facultyEmail="+facultyEmail;
	 }
	 
	 @PostMapping("/submitData")
	 public String handleFormSubmit(@RequestParam("fees") int additionalFees, 
	                                @RequestParam("studentEmail") String email, 
	                                Model model) {
	     System.out.println(" additional fee: " + additionalFees);
	     
	     StudentModel student = studentRepo.findByEmail(email);
	     
	     if (student != null) {
	         int currentFees = student.getFees();
	         int updatedFees = currentFees + additionalFees;
	         student.setFees(updatedFees);
	         
	         studentRepo.save(student);
	         model.addAttribute("students", student);
	         model.addAttribute("successMessage", "Fees updated successfully!");
	     } else {
	         model.addAttribute("errorMessage", "Student not found!");
	     }
	     return "payFees";
	 }



    
    
    @GetMapping("/StudentProfiles")
    public String login() {
    	System.out.println("dyofjhj");
    	return "StudentProfiles";
    }

    @GetMapping("/addDetail")
    public String showAddDetailPage( Model model , @RequestParam String email) {
    	
    	AdminModel admins = adminRepo.findByEmail(email);
    	model.addAttribute("admins", admins);
        return "addDetail"; 
    }
    

}