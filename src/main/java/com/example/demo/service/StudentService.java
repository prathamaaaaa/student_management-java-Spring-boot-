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
    
    private  ExampleRepository exampleRepository;

    @Autowired
    public StudentService(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    public List<ExampleModel> getAllStudents() {
        return exampleRepository.findAll();  // Fetch all students
    }
}
