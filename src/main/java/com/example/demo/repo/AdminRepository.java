package com.example.demo.repo;


import com.example.demo.model.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminModel, Integer> {

	AdminModel findByEmail(String email);

    
}
