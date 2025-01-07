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
    
	@GetMapping("/Password")
	 public String Password( Model model) {
		
	     return "Password";
	 }
	
	@PostMapping("/forgot")
	 public String forgot( Model model,
			 @RequestParam String email
			 ) {
		
		AdminModel admin = adminRepo.findByEmail(email);
		
		String generatedPassword1 = generatePassword();
		 admin.setPassword(generatedPassword1);
		 adminRepo.save(admin);
	     

	     String emailSubject = "Your updeted Portal Password";
	        String emailText = "Hello " + admin.getName() + ",\n\nYour password for the student portal is: " + generatedPassword1;
	        emailService.sendEmail(email, emailSubject, emailText);
		
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
	 
	 @GetMapping("/facultyPage")
	 public String facultyPage(Model model,
			 @RequestParam String email
			 ) {
     	List<StudentModel> students = studentRepo.findAll();
     	AdminModel admin = adminRepo.findByEmail(email);     		
            model.addAttribute("admins", admin);
	     model.addAttribute("students", students);
	     return "facultyPage"; 
	 }


	 
	 @GetMapping("/FacultyProfile")
	 public String FacultyProfiles(@RequestParam("email") String email, Model model) {
	     FacultyModel faculty = facultyRepo.findByEmail(email);
	     System.out.println(email);
	     model.addAttribute("faculty", faculty);
	     return "FacultyProfile";
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
	 
	 
	 @GetMapping("/update")
	 public String update(@RequestParam("email") String email, Model model) {
	     StudentModel students = studentRepo.findByEmail(email);
	     System.out.println("emial"+email);
	     model.addAttribute("students", students);
	     return "update";
	 }
	 @GetMapping("/updateByFaculty")
	 public String updateByFaculty(@RequestParam("email") String email, Model model) throws JsonMappingException, JsonProcessingException {
	     StudentModel students = studentRepo.findByEmail(email);
	     System.out.println("e : "+email);
	     
	     
	     ObjectMapper objectMapper = new ObjectMapper();
	     List<Integer> marks = objectMapper.readValue(students.getMarks1(), new com.fasterxml.jackson.core.type.TypeReference<List<Integer>>() {});
	     System.out.println("m : "+marks.size());

	     model.addAttribute("marks", marks); 	     
	     model.addAttribute("students", students);
	     
	     return "updateByFaculty";
	 }
	 @PostMapping("/updatedByFaculty")
	 public String updatedByFaculty(@RequestParam("email") String email,
			 @RequestParam String name , 
			 @RequestParam String department,
	         @RequestParam("photo") MultipartFile photo,
	         @RequestParam("marks") String marksJson, // Receive marks as JSON string
	         @RequestParam String division,
			 Model model) throws IOException {
	     StudentModel students = studentRepo.findByEmail(email);
	     System.out.println(email);
	     
	     Path path = Paths.get("src/main/resources/static/" + photo.getOriginalFilename());
	     Files.createDirectories(path.getParent());
	     Files.write(path, photo.getBytes()); 
	     ObjectMapper objectMapper = new ObjectMapper();
	     List<Integer> marks = objectMapper.readValue(marksJson, new com.fasterxml.jackson.core.type.TypeReference<List<Integer>>() {});


	     students.setMarks1(marksJson);
	     students.setName(name);
	     students.setDepartment(department);
	     students.setDivision(division);
	     students.setPhoto("/" + photo.getOriginalFilename());
	     
	     FacultyModel faculty = facultyRepo.findByEmail(email);
	     studentRepo.save(students);
	     model.addAttribute("faculty", faculty);
	     model.addAttribute("students", students);
	     model.addAttribute("marks", marks); 	     

	     return "updateByFaculty";
	}
	
	 
	 @PostMapping("/updated")	
	 public String updated(@RequestParam("email") String email,
			 @RequestParam String name , 
			 @RequestParam String department,
	         @RequestParam("photo") MultipartFile photo,
	         @RequestParam String division,
			 Model model) throws IOException {
	     StudentModel students = studentRepo.findByEmail(email);
	     System.out.println(email);
	     
	     Path path = Paths.get("src/main/resources/static/" + photo.getOriginalFilename());
	     Files.createDirectories(path.getParent());
	     Files.write(path, photo.getBytes()); 
	     
	     
	     students.setName(name);
	     students.setDepartment(department);
	     students.setDivision(division);
	     students.setPhoto("/" + photo.getOriginalFilename());
	     
	     
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
	 

	 @PostMapping("/addStudent")
	 public String addStudent(
	         @RequestParam String name,
	         @RequestParam String email,
	         @RequestParam String department,
	         @RequestParam String division,
//	         @RequestParam int rollNumber,
	         @RequestParam("marks") String marksJson, // Receive marks as JSON string
	         @RequestParam("photo") MultipartFile photo,
	         @RequestParam String role,
	         Model model) throws IOException {

	        String generatedPassword = generatePassword();

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
	     adminModel.setPassword(generatedPassword);
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
	     

	     String emailSubject = "Your Student Portal Password";
	        String emailText = "Hello " + name + ",\n\nYour password for the student portal is: " + generatedPassword;
	        emailService.sendEmail(email, emailSubject, emailText);
	        
	        
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
    private String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        Random random = new Random();
        StringBuilder password = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }
}