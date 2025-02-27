package com.example.demo.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.UserDetailsServiceImpl;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/StuedentFacultyChat/**", "/favicon.ico","/src/main/resources/static/**"
            				,"/chat-websocket","/addDetail","/static","/ws/**", "/chat/**"
            				,"/updated","/updateByFaculty","/StudentsChat/**","/StudentsChat"
            				,"/StuedentFacultyChat","/chat-websocket/**"
            				,"/submitData","/searchStudents","/*"
            				, "/*.png", "/*.jpg", "/*.jpeg","/adminpanel","/admin/students","/students"
            				,"/update","/updatedByFaculty","/FacultyChat/**","/FacultyChat","/Password","/payFees"
            				,"/register","/StudentProfile","/reportCard","/StudentPage","/FacultyProfile"
            				,"/facultyPage","/addStudent", "/login", "/login/check", "/register"
            				, "/submitForm").permitAll()
                    .anyRequest().authenticated() 

            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(List.of(provider));
    }
}
