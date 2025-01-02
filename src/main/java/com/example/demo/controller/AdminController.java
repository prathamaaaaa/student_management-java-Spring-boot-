package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.AdminModel;
import com.example.demo.model.StudentModel;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.StudentRepository;
import com.example.demo.service.AdminService;
import com.example.demo.validation.adminValidation;
import com.fasterxml.jackson.databind.ObjectMapper;

import aj.org.objectweb.asm.TypeReference;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;


@Controller
public class AdminController {
    
	@Autowired
	private AdminRepository adminRepo;
	
	@Autowired
	private StudentRepository studentRepo;

    @Autowired
    private AdminService adminService;

    @Autowired
    private adminValidation adminValidation;

    
	public String photoUrl;
    
    
	@GetMapping("/register")
	public String adminPage(Model model) {
	    model.addAttribute("adminModel", new AdminModel()); 
	    return "register"; 
	}
	 @PostMapping("/submitForm")
	    public String submitForm(@Valid @ModelAttribute("adminModel") AdminModel adminModel,
	                             BindingResult result,
	                             Model model) {

	        if (result.hasErrors()) {
	            model.addAttribute("error", "There are errors in the form. Please correct them.");
	            return "register"; // Show registration form again if errors
	        }

	        adminService.saveAdminData(adminModel);

	        return "redirect:/login"; // Redirect to login page after successful registration
	    } 
	 
	 @PostMapping("/addStudent")
	 public String addStudent(
	         @RequestParam String name,
	         @RequestParam String email,
	         @RequestParam String password,
	         @RequestParam String department,
	         @RequestParam String division,
	         @RequestParam("marks") String marksJson, // Receive marks as JSON string
	         @RequestParam("photo") MultipartFile photo,
	         @RequestParam String role,
	         Model model) throws IOException {

	     // Process photo upload
	     Path path = Paths.get("src/main/resources/static/" + photo.getOriginalFilename());
	     Files.createDirectories(path.getParent());
	     Files.write(path, photo.getBytes()); 

	     // Parse marks JSON string
	     ObjectMapper objectMapper = new ObjectMapper();
	     List<Integer> marks = objectMapper.readValue(marksJson, new com.fasterxml.jackson.core.type.TypeReference<List<Integer>>() {});

	     AdminModel adminModel = new AdminModel();
	     adminModel.setName(name);
	     adminModel.setEmail(email);
	     adminModel.setRole(role);
	     adminModel.setPassword(password);
	     AdminModel savedAdmin = adminRepo.save(adminModel);

	     StudentModel student = new StudentModel();
	     student.setName(name);
	     student.setEmail(email);
	     student.setDepartment(department);
	     student.setDivision(division);
	     student.setMarks1(marksJson); 
	     student.setPhoto("/" + photo.getOriginalFilename());
	     student.setAdmin(savedAdmin);

	     studentRepo.save(student);

	     model.addAttribute("successMessage", "Student added successfully!");
	     return "addDetail";
	 }

    @GetMapping("/viewAdmins")
    public String viewAdmins(Model model) {
    	List<AdminModel> admins = adminRepo.findAll();
        for (AdminModel adminModel : admins) {
        	System.out.println(adminModel.getName());
			
		}
        model.addAttribute("admins", admins);

        return "viewAdmins";
    }
    
    @GetMapping("/login")
    public String login(Model adminModel) {
    	return "login";
		
    }

    @GetMapping("/addDetail")
    public String showAddDetailPage() {
        return "addDetail"; 
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
            	StudentModel sm = new StudentModel();
            	
            	List<StudentModel> students = studentRepo.findAll();
                model.addAttribute("students", students);
                model.addAttribute("photoUrl", photoUrl); // Add the photo URL to the model
            	return "facultyPage";  
            }
            return "StudentPage";
            
        } else {
        	return "login";  
        	
        }	
    }
}