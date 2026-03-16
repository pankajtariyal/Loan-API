package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.dto.update.PasswordUpdateByUserRequest;
import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.jwttoken.JwtUtils;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, EmailService emailService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager  = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
    }

    public String updateUserPassword(PasswordUpdateByUserRequest passwordUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));
        boolean bool = passwordEncoder.matches(passwordUpdate.getPassword(),user.getPassword());
        if(bool){
            user.setPassword(passwordEncoder.encode(passwordUpdate.getNew_password()));
            user.setUpdated_at(Timestamp.valueOf(LocalDateTime.now()));
            userRepository.save(user);
            return "Password Updated Successfully";
        }
        else{
            return "wrong password.";
        }
    }

    public UserResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
        return UserResponse.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .created_at(user.getCreated_at().toString())
                .created_by(user.getCreated_by())
                .updated_at(user.getUpdated_at().toString())
                .updated_at(user.getUpdated_by())
                .build();
    }
}
