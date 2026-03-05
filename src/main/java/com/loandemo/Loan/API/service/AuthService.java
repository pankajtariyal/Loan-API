package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.dto.login.LoginRequestDto;
import com.loandemo.Loan.API.dto.login.LoginResponse;
import com.loandemo.Loan.API.dto.register.UserRegistrationRequest;
import com.loandemo.Loan.API.dto.register.UserRegistrationResponse;
import com.loandemo.Loan.API.jwttoken.JwtUtils;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.repository.UserRepository;
import com.loandemo.Loan.API.responseapi.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, EmailService emailService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager  = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
    }

    @Transactional
    public ResponseEntity<APIResponse<UserRegistrationResponse>> createUser(UserRegistrationRequest registrationRequest) throws Exception {
        try{
            boolean isUserExist = userRepository.existsUserByUsername(registrationRequest.getUsername());  // checking for if usernam already exits or not
            boolean isEmailExist = userRepository.existsUserByEmail(registrationRequest.getEmail());  // checking that email already exist
            if(isUserExist){
                return ResponseEntity.ok(APIResponse.error("user already exist."));
            } else if (isEmailExist) {
                return ResponseEntity.ok(APIResponse.error("Someone already exist with this email."));
            }

            User user = User.builder()
                    .username(registrationRequest.getUsername())
                    .email(registrationRequest.getEmail())
                    .password(passwordEncoder.encode(registrationRequest.getPassword()))
                    .role("USER")
                    .isActive(true)
                    .created_at(Timestamp.valueOf(LocalDateTime.now()))
                    .created_by(registrationRequest.getUsername())
                    .updated_at(Timestamp.valueOf(LocalDateTime.now()))
                    .updated_by(registrationRequest.getUsername())
                    .verification(false)
                    .build();

            User saveUser = userRepository.save(user);
            emailService.saveAndSendToken(saveUser);
            UserRegistrationResponse registrationResponse = UserRegistrationResponse.builder()
                    .username(saveUser.getUsername())
                    .build();
            return ResponseEntity.ok(APIResponse.success(registrationResponse,"User register successfully and email verification link sent to you email."));
        }catch (Exception exception){
            throw new Exception("Internal error");
        }
    }

    public ResponseEntity<APIResponse<LoginResponse>> checkLogin(LoginRequestDto loginRequestDto){
        System.out.println(loginRequestDto);
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

        if(authentication.isAuthenticated()){
            String role = authentication.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse(null);
            LoginResponse loginResponse = LoginResponse.builder()
                    .username(authentication.getName())
                    .token(jwtUtils.generateToken(authentication.getName(),role)).build();
            return ResponseEntity.ok(APIResponse.success(loginResponse,"user is authenticate"));
        }else{
            return ResponseEntity.ok(APIResponse.error("user is not authenticate"));
        }
    }
}
