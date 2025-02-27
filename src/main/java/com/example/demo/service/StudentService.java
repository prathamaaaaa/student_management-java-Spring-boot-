package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.AdminModel;
import com.example.demo.model.ExampleModel;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.ExampleRepository;

@Service
public class StudentService {
    
//    private  ExampleRepository exampleRepository;
	private AdminRepository adminRepository;
	@Autowired
    public StudentService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

//    @Autowired
//    public StudentService(ExampleRepository exampleRepository) {
//        this.exampleRepository = exampleRepository;
//    }

    public List<AdminModel> getAllStudents() {
        return adminRepository.findAll();  // Fetch all students
    }
}
