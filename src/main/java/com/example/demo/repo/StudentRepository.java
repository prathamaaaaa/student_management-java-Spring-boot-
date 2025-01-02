package com.example.demo.repo;

import com.example.demo.model.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentModel, Integer> {
}
