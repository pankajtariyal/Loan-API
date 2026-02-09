package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.modul.UserRequest;
import com.loandemo.Loan.API.modul.UserResponseDto;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("register")
    public ResponseEntity<APIResponse<UserResponseDto>> registerUser(@RequestBody UserRequest userRequest){
        return service.createUser(userRequest);
    }

    @PostMapping("update")
    public ResponseEntity<APIResponse<String>> updateUserPassword(@RequestBody UserRequest userRequest){
        return service.updateUserPassword(userRequest);
    }
}
