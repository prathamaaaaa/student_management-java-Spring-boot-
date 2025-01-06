package com.example.demo.repo;

import com.example.demo.model.AdminModel;

import com.example.demo.model.StudentModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<StudentModel, Integer> {
	
	StudentModel findByEmail(String email);
//	StudentModel findByRollNumber(int rollNumber);
//  List<StudentModel> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
    List<StudentModel> findByNameContainingIgnoreCase(String name);
//  List<StudentModel> findByDivisionAndDepartment(String division, String department);

}
