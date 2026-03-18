package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.dto.update.PasswordUpdateByUserRequest;
import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.jwttoken.JwtUtils;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Service class responsible for handling user-related operations
 * in the Loan Management System.
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Update user password</li>
 *     <li>Fetch authenticated user details</li>
 * </ul>
 *
 * <p>This service interacts with Spring Security context to retrieve
 * the currently authenticated user.</p>
 *
 * <p>Security Context Flow:</p>
 * <pre>
 * User logs in →
 * Authentication stored in SecurityContext →
 * Service retrieves current user →
 * Performs operations (update password / fetch details)
 * </pre>
 *
 * @author Abhishek
 */
@Service
public class UserService {

    /**
     * Repository for user-related database operations.
     */
    private final UserRepository userRepository;

    /**
     * Password encoder for hashing and verifying passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Authentication manager for handling authentication processes.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Utility class for JWT token operations.
     */
    private final JwtUtils jwtUtils;

    /**
     * Service for handling email-related operations.
     */
    private final EmailService emailService;

    /**
     * Constructor for dependency injection.
     *
     * @param userRepository user repository
     * @param passwordEncoder password encoder
     * @param authenticationManager authentication manager
     * @param jwtUtils JWT utility
     * @param emailService email service
     */
    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils,
                       EmailService emailService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager  = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
    }

    /**
     * Updates the password of the currently authenticated user.
     *
     * <p>This method performs the following steps:</p>
     * <ul>
     *     <li>Fetches current authenticated user from SecurityContext</li>
     *     <li>Validates current password using {@link PasswordEncoder}</li>
     *     <li>If valid → updates password with encoded new password</li>
     *     <li>Updates modification timestamp</li>
     * </ul>
     *
     * <p>If the current password does not match, the update is rejected.</p>
     *
     * @param passwordUpdate request containing current and new password
     * @return success or failure message
     */
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
            throw new IllegalArgumentException("Invalid credentials");
        }
    }

    /**
     * Retrieves details of the currently authenticated user.
     *
     * <p>This method fetches user information from the database
     * and maps it into {@link UserResponse} DTO.</p>
     *
     * <p>Returned details include:</p>
     * <ul>
     *     <li>User ID</li>
     *     <li>Username</li>
     *     <li>Email</li>
     *     <li>Role</li>
     *     <li>Account status</li>
     *     <li>Audit fields (created/updated info)</li>
     * </ul>
     *
     * @return {@link UserResponse} containing user details
     */
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
                .updated_by(user.getUpdated_by())
                .build();
    }
}
