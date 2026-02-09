package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.modul.UserRequest;
import com.loandemo.Loan.API.modul.UserResponseDto;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.UserService;
import org.hibernate.hql.internal.classic.AbstractParameterInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<APIResponse<UserResponseDto>> login(@RequestBody UserRequest userRequest){
        return userService.checkLogin(userRequest);
    }
}
