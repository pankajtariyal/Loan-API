package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.login.LoginRequestDto;
import com.loandemo.Loan.API.dto.login.LoginResponse;
import com.loandemo.Loan.API.dto.register.UserRegistrationRequest;
import com.loandemo.Loan.API.dto.register.UserRegistrationResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name="Auth APIs",description = "Login and Registration Operation")
@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "User Login", description = "User will enter username and password if success wil get token")
    @PostMapping("login")
    public ResponseEntity<APIResponse<LoginResponse>> login(@Valid @RequestBody
                                                                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                        description = "Login request payload",
                                                                        required = true
                                                                ) LoginRequestDto loginRequestDto){
        return authService.checkLogin(loginRequestDto);
    }

    @Operation(summary = "User registration", description = "For registration new user")
    @PostMapping("register")
    public ResponseEntity<APIResponse<UserRegistrationResponse>> registerUser(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody( description = "User Registration Modal",
                    required = true )UserRegistrationRequest registrationRequest) throws Exception {
        return authService.createUser(registrationRequest);
    }
}
