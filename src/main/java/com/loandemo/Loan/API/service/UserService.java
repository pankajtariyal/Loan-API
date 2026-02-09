package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.jwttoken.JwtUtils;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.modul.UserRequest;
import com.loandemo.Loan.API.modul.UserResponseDto;
import com.loandemo.Loan.API.repository.UserRepository;
import com.loandemo.Loan.API.responseapi.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public ResponseEntity<APIResponse<UserResponseDto>> createUser(UserRequest userRequest){
        boolean isUserExist = userRepository.existsUserByUsername(userRequest.getUsername());
        if(isUserExist){
            return ResponseEntity.ok(APIResponse.error("user already exist."));
        }
        User user = User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password_hash(passwordEncoder.encode(userRequest.getPassword()))
                .role(userRequest.getRole())
                .isActive(userRequest.is_active())
                .created_at(Timestamp.valueOf(LocalDateTime.now()))
                .created_by(userRequest.getCreated_by())
                .updated_at(Timestamp.valueOf(LocalDateTime.now()))
                .updated_by(userRequest.getUpdated_by())
                .build();
        User saveUser = userRepository.save(user);
        UserResponseDto userResponseDto = UserResponseDto.builder().username(saveUser.getUsername()).role(saveUser.getRole()).build();
        return ResponseEntity.ok(APIResponse.success(userResponseDto,"User register successfully."));
    }

    public ResponseEntity<APIResponse<UserResponseDto>> checkLogin(UserRequest userRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(),userRequest.getPassword()));
        if(authentication.isAuthenticated()){
            System.out.println("principle "+authentication.getPrincipal());
            UserResponseDto userResponseDto = UserResponseDto.builder()
                    .jwtToken(jwtUtils.generateToken(authentication.getName())).build();
            return ResponseEntity.ok(APIResponse.success(userResponseDto,"user is authenticate"));
        }else{
            return ResponseEntity.ok(APIResponse.error("user is not authenticate"));
        }
    }

    public ResponseEntity<APIResponse<String>> updateUserPassword(UserRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            user.setPassword_hash(passwordEncoder.encode(userRequest.getPassword()));
            user.setUpdated_at(Timestamp.valueOf(LocalDateTime.now()));
            user.setUpdated_by(username);
            userRepository.save(user);
            return ResponseEntity.ok(APIResponse.success("User Password updated"));
        }else{
            return ResponseEntity.ok(APIResponse.error("User not found."));
        }
    }
}
