package com.example.demo.controller;

import com.example.demo.model.AdminModel;
import com.example.demo.model.StudentModel;
import com.example.demo.repo.AdminRepository;
import com.example.demo.repo.FacultyRepository;
import com.example.demo.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentFacultyController {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private FacultyRepository facultyRepo;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/StuedentFacultyChat")
    public String StuedentFacultyChat(
            @RequestParam("sender") String sender,
            @RequestParam("reciever") String reciever,
            Model model){

//        AdminModel admin = adminRepository.findByEmail(email);
//        AdminModel adminf = adminRepository.findByEmail(facultyEmail);
        model.addAttribute("sender", sender);

        model.addAttribute("reciever", reciever);

        return "/StuedentFacultyChat";
    }

}
