package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.update.PasswordUpdateByUserRequest;
import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(
        name="User APIs",
        description = "Operation related to User")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "Update USer", description = "Update Existing user password")
    @PostMapping("update")
    public ResponseEntity<APIResponse<String>> updateUserPassword(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User Password update request",
                    required = true
            ) PasswordUpdateByUserRequest passwordUpdate){
        String result = userService.updateUserPassword(passwordUpdate);
        return ResponseEntity.ok(APIResponse.success(result));
    }

    @Operation(summary = "User Detail", description = "User detail get")
    @GetMapping("get/user")
    public ResponseEntity<APIResponse<UserResponse>> getUser(){
        UserResponse userResponse = userService.getUser();
        return ResponseEntity.ok(APIResponse.success(userResponse));
    }

}
