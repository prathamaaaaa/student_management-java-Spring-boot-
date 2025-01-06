package com.example.demo.service;

import com.example.demo.model.AdminModel;

import com.example.demo.model.FacultyModel;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    public void saveAdminData(AdminModel adminModel) {
        AdminModel savedAdmin = adminRepository.save(adminModel);

        FacultyModel facultyModel = new FacultyModel();
        facultyModel.setName(adminModel.getName());
        facultyModel.setEmail(adminModel.getEmail());
        facultyModel.setPassword(adminModel.getPassword());
        facultyModel.setAdmin(savedAdmin);  
        
        facultyRepository.save(facultyModel);
    }
}
