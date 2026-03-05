package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.update.PasswordUpdateByUserRequest;
import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("update")
    public ResponseEntity<APIResponse<String>> updateUserPassword(@Valid @RequestBody PasswordUpdateByUserRequest passwordUpdate){
        String result = userService.updateUserPassword(passwordUpdate);
        return ResponseEntity.ok(APIResponse.success(result));
    }

    @GetMapping("get/user")
    public ResponseEntity<APIResponse<UserResponse>> getUser(){
        UserResponse userResponse = userService.getUser();
        return ResponseEntity.ok(APIResponse.success(userResponse));
    }

}
