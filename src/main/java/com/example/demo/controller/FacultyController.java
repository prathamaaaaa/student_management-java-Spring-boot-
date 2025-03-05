package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.AdminModel;
import com.example.demo.model.FacultyModel;
import com.example.demo.model.StudentModel;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.FacultyRepository;
import com.example.demo.repo.StudentRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.AdminService;
import com.example.demo.service.EmailService;
import com.example.demo.validation.adminValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
@RequestMapping("/all")
public class FacultyController {
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

	@Autowired
	private JwtUtil jwtutil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	
	 @GetMapping("/facultyPage")
	 public String facultyPage(Model model,
	         @RequestParam String facultyEmail
	       ) {
     	List<StudentModel> students = studentRepo.findAll();
     	AdminModel admins = adminRepo.findByEmail(facultyEmail);
     	FacultyModel faculty = facultyRepo.findByEmail(facultyEmail);
            model.addAttribute("admins", admins);
	     model.addAttribute("students", students);
	     model.addAttribute("faculty", faculty);
	     return "facultyPage"; 
	 }

	 @GetMapping("/updateByFaculty")
	 public String updateByFaculty(
	         @RequestParam("email") String email, 
	         @RequestParam("facultyEmail") String facultyEmail, 
	         Model model) throws JsonMappingException, JsonProcessingException {
	     
	     StudentModel students = studentRepo.findByEmail(email);
	     System.out.println("Student Email: " + email);

	     System.out.println("Faculty Email: " + facultyEmail);

	     ObjectMapper objectMapper = new ObjectMapper();
	     List<Integer> marks = objectMapper.readValue(
	             students.getMarks1(), 
	             new com.fasterxml.jackson.core.type.TypeReference<List<Integer>>() {}
	     );
	     System.out.println("Marks Size: " + marks.size());

	     // Add attributes to the model
	     model.addAttribute("marks", marks);
	     model.addAttribute("students", students);
	     model.addAttribute("facultyEmail", facultyEmail); // Add faculty email to model if needed

	     return "updateByFaculty";
	 }
	 
	 @GetMapping("/FacultyProfile")
	 public String FacultyProfiles(@RequestParam("email") String email, Model model) {
	     FacultyModel faculty = facultyRepo.findByEmail(email);
	     System.out.println(email);
	     model.addAttribute("faculty", faculty);
	     return "FacultyProfile";
	 }
	 
	 
	 @PostMapping("/updatedByFaculty")
	 public String updatedByFaculty(@RequestParam("email") String email,
			 @RequestParam String name , 
			 @RequestParam String department,
	         @RequestParam(value = "photo", required = false) MultipartFile photo,  
	         @RequestParam("marks") String marksJson, // Receive marks as JSON string
	         @RequestParam String division,
	         @RequestParam("facultyEmail") String facultyEmail,
			 Model model) throws IOException {
		 
	     StudentModel students = studentRepo.findByEmail(email);
	     System.out.println(email);
	     
	     if (photo != null && !photo.isEmpty()) {
//	         Path path = Paths.get("src/main/resources/static/" + photo.getOriginalFilename());
//	         Files.createDirectories(path.getParent());
//	         Files.write(path, photo.getBytes());
	         // Convert photo to Base64
		     String base64Photo = null;
		     if (photo != null && !photo.isEmpty()) {
		         base64Photo = Base64.getEncoder().encodeToString(photo.getBytes());
		     }
//	         students.setPhoto("/" + photo.getOriginalFilename());
	         students.setPhoto(base64Photo);
	         System.out.println("New photo uploaded: " + photo.getOriginalFilename());
	     } else {
	         System.out.println("No new photo uploaded. Keeping the existing photo.");
	     }
	      
	     ObjectMapper objectMapper = new ObjectMapper();
	     List<Integer> marks = objectMapper.readValue(marksJson, new com.fasterxml.jackson.core.type.TypeReference<List<Integer>>() {});


	     students.setMarks1(marksJson);
	     students.setName(name);
	     students.setDepartment(department);
	     students.setDivision(division);
	     AdminModel admins = adminRepo.findByEmail(facultyEmail);
	     FacultyModel faculty = facultyRepo.findByEmail(facultyEmail);
	     studentRepo.save(students);
	     model.addAttribute("faculty", faculty);
	     model.addAttribute("admins", admins);
	     model.addAttribute("students", students);
	     model.addAttribute("marks", marks); 	     

	     return "redirect:/all/facultyPage?facultyEmail=" + facultyEmail;
	}
	
	 
	 @PostMapping("/addStudent")
	 public String addStudent(
	         @RequestParam String name,
	         @RequestParam String email,
	         @RequestParam String department,
	         @RequestParam String division,
//	         @RequestParam int rollNumber,
//	         @RequestParam int id,
	         @RequestParam("marks") String marksJson, // Receive marks as JSON string
	         @RequestParam("photo") MultipartFile photo,
	         @RequestParam String role,
	         @RequestParam String facultyEmail,
//	         @RequestParam FacultyModel fid,
	         @RequestParam("facultyId") int facultyId,
	        
	         Model model) throws IOException {

	        String generatedPassword = generatePassword();
	        String emailSubject = "Your Student Portal Password";
	        String emailText = "Hello " + name + ",\n\nYour password for the student portal is: " + generatedPassword;
	        emailService.sendEmail(email, emailSubject, emailText);
	        
//	        String generatedToken = jwtutil.generateToken(generatedPassword);
	        String encodedToken = passwordEncoder.encode(generatedPassword);
	     // Process photo upload
	        Path path = Paths.get("src/main/resources/static/" + photo.getOriginalFilename());
		     Files.createDirectories(path.getParent());
		     Files.write(path, photo.getBytes()); 


		     
		     // Convert photo to Base64
		     String base64Photo = null;
		     if (photo != null && !photo.isEmpty()) {
		         base64Photo = Base64.getEncoder().encodeToString(photo.getBytes());
		     }
		     
		     
	     // Parse marks JSON string
	     ObjectMapper objectMapper = new ObjectMapper();
	     List<Integer> marks = objectMapper.readValue(marksJson, new com.fasterxml.jackson.core.type.TypeReference<List<Integer>>() {});

	     AdminModel adminModel = new AdminModel();
	     adminModel.setName(name);
	     adminModel.setEmail(email);
	     adminModel.setRole(role);
	     adminModel.setPassword(encodedToken);
	 
	     AdminModel savedAdmin = adminRepo.save(adminModel);
	    
	     
	     fees=0;
	     totalMarks = 0;
	     percentage =0;
	     for (int mark : marks) {
	         totalMarks += mark;
	     }
	     
	     percentage = totalMarks/marks.size();

	     System.out.println("Total Marks: " + totalMarks);
	     
	     
	     FacultyModel faculty = facultyRepo.findByEmail(facultyEmail);
	     FacultyModel facultyid = facultyRepo.findById(facultyId);
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
//	     student.setPhoto("/" + photo.getOriginalFilename());
	     student.setPhoto(base64Photo); // Store Base64 photo

	     student.setAdmin(savedAdmin);
	     student.setFaculty(facultyid);

	     studentRepo.save(student);
	     
	     
	     
	        
	        
	     model.addAttribute("successMessage", "Student added successfully!");

	     
	     return "redirect:/all/addDetail?email=" + facultyEmail;
	 
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
