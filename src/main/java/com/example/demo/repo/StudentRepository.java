package com.example.demo.repo;

import com.example.demo.model.AdminModel;
import com.example.demo.model.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentModel, Integer> {
	
	StudentModel findByEmail(String email);
//	StudentModel findByRollNumber(int rollNumber);


}
