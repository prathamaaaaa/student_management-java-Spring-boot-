package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.StudentModel;
import com.example.demo.repo.StudentRepository;


@Service
public class StudentService {
	 private final StudentRepository studentRepository;

	    public StudentService(StudentRepository studentRepository) {
	        this.studentRepository = studentRepository;
	    }

//	    public List<StudentModel> searchByName(String name) {
//	        return studentRepository.findByNameContaining(name);
//	    }
}
