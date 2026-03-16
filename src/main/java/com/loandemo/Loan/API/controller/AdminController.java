package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name="Admin APIs", description = "Admin Operations")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @Operation(summary = "User list", description = "Get all user detail (Admin can access this)")
    @GetMapping("all/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<UserResponse>>> getAllUserList(){
        List<UserResponse> userResponseList = adminService.getAllUsers();
        return ResponseEntity.ok(APIResponse.success(userResponseList,"All user"));
    }
}
