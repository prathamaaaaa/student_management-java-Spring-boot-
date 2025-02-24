package com.example.demo.security;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.AdminModel;
import com.example.demo.repo.AdminRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AdminRepository adminRepository;

    public UserDetailsServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminModel admin = adminRepository.findByEmail(email);

        if (admin == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.withUsername(admin.getEmail())
                .password(admin.getPassword())
                .roles(admin.getRole())
                .build();
    }
}
