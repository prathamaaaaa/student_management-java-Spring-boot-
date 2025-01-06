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

	public int fees;

	public String email;

	public int totalMarks;

	public int percentage;
    
    
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
	            return "register"; 
	        }

	        adminService.saveAdminData(adminModel);

	        return "redirect:/login"; 
	    } 
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

	 @GetMapping("/reportCard")
	 public String reportCard(@RequestParam("email") String email, Model model) {
	     StudentModel students = studentRepo.findByEmail(email);
	     System.out.println(email);
	     model.addAttribute("students", students);
	     return "reportCard";
	 }
	 @PostMapping("/addStudent")
	 public String addStudent(
	         @RequestParam String name,
	         @RequestParam String email,
	         @RequestParam String password,
	         @RequestParam String department,
	         @RequestParam String division,
//	         @RequestParam int rollNumber,
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

	     fees=0;
	     totalMarks = 0;
	     percentage =0;
	     for (int mark : marks) {
	         totalMarks += mark;
	     }
	     
	     percentage = totalMarks/marks.size();

	     System.out.println("Total Marks: " + totalMarks);
	     
	     StudentModel student = new StudentModel();
	     student.setName(name);
	     student.setEmail(email);
	     student.setDepartment(department);
	     student.setDivision(division);
	     student.setMarks1(marksJson); 
//	     student.setRollNumber(rollNumber);
	     student.setFees(fees);
	     student.setPercentage(percentage);
	     student.setTotalMarks(totalMarks);
	     student.setPhoto("/" + photo.getOriginalFilename());
	     student.setAdmin(savedAdmin);

	     studentRepo.save(student);

	     model.addAttribute("successMessage", "Student added successfully!");
	     return "addDetail";
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
            	List<StudentModel> students = studentRepo.findAll();
            	model.addAttribute("students", students);
            	StudentModel sm = new StudentModel();
            	return "facultyPage";  
            }
            if(admin.getRole().equalsIgnoreCase("USER"))
            {
            	StudentModel students = studentRepo.findByEmail(email);
            	model.addAttribute("students", students);
            	StudentModel sm = new StudentModel();
                return "StudentPage";
            }
            
            
            return "StudentPage";
            
        } else {
        	return "login";  
        	
        }	
    }
}