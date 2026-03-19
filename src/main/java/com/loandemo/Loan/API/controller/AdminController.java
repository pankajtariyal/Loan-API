package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.AdminService;
import com.loandemo.Loan.API.uitls.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * REST Controller responsible for handling Admin related operations.
 *
 * <p>This controller provides APIs that are accessible only to users
 * with ADMIN role. It allows administrators to manage and view system data.
 *
 * <p>Currently supported operations:
 * <ul>
 *     <li>Retrieve list of all registered users</li>
 * </ul>
 *
 * @apiNote All endpoints require JWT authentication with ADMIN privileges.
 *
 * @implNote This controller delegates business logic to {@link AdminService}.
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Tag(name="Admin APIs", description = "Operations restricted to admin users")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("admin")
public class AdminController {
    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    /**
     * Retrieves the list of all registered users.
     *
     * <p>This API is restricted to ADMIN users and returns
     * basic details of all users in the system.
     *
     * @return {@link APIResponse} containing a list of {@link UserResponse}
     *
     * @apiNote Only users with ADMIN role can access this endpoint.
     *
     * @implSpec This method performs the following steps:
     * <ol>
     *     <li>Validates ADMIN authorization</li>
     *     <li>Fetches all users from the database</li>
     *     <li>Maps entities to {@link UserResponse} DTOs</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(summary = "User list", description = "Retrieve list of all users (Admin only)")
    @GetMapping("all/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<UserResponse>>> getAllUserList(){
        logger.info("Request to retrieve all user form admin: {}", SecurityUtil.getCurrentUser());
        List<UserResponse> userResponseList = adminService.getAllUsers();
        logger.info("Retrieve all user successfully for admin: {}", SecurityUtil.getCurrentUser());
        return ResponseEntity.ok(APIResponse.success(userResponseList,"All user"));

    }
}
