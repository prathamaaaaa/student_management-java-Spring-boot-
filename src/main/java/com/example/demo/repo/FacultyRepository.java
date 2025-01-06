package com.example.demo.repo;

import com.example.demo.model.FacultyModel;
import com.example.demo.model.StudentModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<FacultyModel, Integer> {
	
	FacultyModel findByEmail(String email);

}
