package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.login.LoginRequestDto;
import com.loandemo.Loan.API.dto.UserRequestDto;
import com.loandemo.Loan.API.dto.login.LoginResponse;
import com.loandemo.Loan.API.dto.register.UserRegistrationRequest;
import com.loandemo.Loan.API.dto.register.UserRegistrationResponse;
import com.loandemo.Loan.API.dto.update.PasswordUpdateByUserRequest;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.AuthService;
import com.loandemo.Loan.API.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public ResponseEntity<APIResponse<LoginResponse>> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return authService.checkLogin(loginRequestDto);
    }

    @PostMapping("register")
    public ResponseEntity<APIResponse<UserRegistrationResponse>> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) throws Exception {
        return authService.createUser(registrationRequest);
    }
}
