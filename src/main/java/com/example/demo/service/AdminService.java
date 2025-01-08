package com.example.demo.service;

import com.example.demo.model.AdminModel;

import com.example.demo.model.FacultyModel;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    public void saveAdminData(AdminModel adminModel ,@RequestParam String mobileNumber) {
        AdminModel savedAdmin = adminRepository.save(adminModel);

        FacultyModel facultyModel = new FacultyModel();
        facultyModel.setName(adminModel.getName());
        facultyModel.setEmail(adminModel.getEmail());
        facultyModel.setPassword(adminModel.getPassword());
        facultyModel.setAdmin(savedAdmin); 
        facultyModel.setMobileNumber(mobileNumber);
        
        facultyRepository.save(facultyModel);
    }
}
