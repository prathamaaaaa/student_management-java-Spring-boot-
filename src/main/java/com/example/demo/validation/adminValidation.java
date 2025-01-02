package com.example.demo.validation;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.model.AdminModel;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@Component
public class adminValidation {
	public void validateAdmin(AdminModel admin) {
	    Set<ConstraintViolation<AdminModel>> violations = validator.validate(admin);
	    if (!violations.isEmpty()) {
	        throw new ConstraintViolationException(violations);
	    }
	}
	   @Autowired
	    private Validator validator;

	  
	    @PostMapping("/create")
	    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminModel adminModel, BindingResult result) {
	        if (result.hasErrors()) {
	            return ResponseEntity.badRequest().body(result.getAllErrors()); 
	        }
	        return ResponseEntity.ok("Admin created successfully!");
	    }
}
