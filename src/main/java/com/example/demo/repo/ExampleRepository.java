package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.AdminModel;
import com.example.demo.model.ExampleModel;

@Repository
public interface ExampleRepository extends JpaRepository<ExampleModel, Integer> {

}