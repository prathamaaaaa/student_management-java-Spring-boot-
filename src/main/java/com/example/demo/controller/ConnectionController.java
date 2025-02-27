package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.AdminModel;
import com.example.demo.model.ExampleModel;
import com.example.demo.service.StudentService;

@RestController
@RequestMapping("/students")
public class ConnectionController {
    
    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<AdminModel> getAllStudents() {
        return studentService.getAllStudents();  // Return list of students
    }
}
