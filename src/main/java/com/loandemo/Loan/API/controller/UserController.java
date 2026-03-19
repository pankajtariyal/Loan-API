package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.update.PasswordUpdateByUserRequest;
import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.UserService;
import com.loandemo.Loan.API.uitls.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller responsible for handling User-related operations.
 *
 * <p>This controller provides APIs for:
 * <ul>
 *     <li>Updating user password</li>
 *     <li>Fetching authenticated user details</li>
 * </ul>
 *
 * <p>All endpoints require JWT authentication.
 *
 * @apiNote These APIs operate on the currently authenticated user.
 *
 * @implNote This controller delegates user-related business logic to {@link UserService}.
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Tag(
        name = "User APIs",
        description = "Operations related to user management"
)
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Updates the password of the authenticated user.
     *
     * <p>This API allows users to securely update their password
     * by providing the required credentials in the request body.
     *
     * @param passwordUpdate request payload containing old and new password
     * @return {@link APIResponse} containing success message
     *
     * @apiNote The user must be authenticated with a valid JWT token.
     *
     * @implSpec This method performs the following steps:
     * <ol>
     *     <li>Validates current password</li>
     *     <li>Checks new password constraints</li>
     *     <li>Updates password in database</li>
     *     <li>Returns success message</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "Update User Password",
            description = "Update password of the authenticated user"
    )
    @PostMapping("update")
    public ResponseEntity<APIResponse<String>> updateUserPassword(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User password update request payload",
                    required = true
            )
            PasswordUpdateByUserRequest passwordUpdate){
        logger.info("Password updated of user: {}", SecurityUtil.getCurrentUser());
        try{
            String result = userService.updateUserPassword(passwordUpdate);
            logger.info("{} of user: {}",result, SecurityUtil.getCurrentUser());
            return ResponseEntity.ok(APIResponse.success(result));
        }catch (Exception e){
            logger.error("Failed to update password of user: {}", SecurityUtil.getCurrentUser());
            throw new IllegalArgumentException("User not found or invalid credential");
        }
    }

    /**
     * Fetches details of the authenticated user.
     *
     * <p>This API returns user profile information such as
     * name, email, and other registered details.
     *
     * @return {@link APIResponse} containing {@link UserResponse}
     *
     * @apiNote The user must be authenticated with a valid JWT token.
     *
     * @implSpec This method:
     * <ol>
     *     <li>Identifies the authenticated user</li>
     *     <li>Fetches user details from database</li>
     *     <li>Maps entity to response DTO</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "Get User Details",
            description = "Fetch details of the authenticated user"
    )
    @GetMapping("get/user")
    public ResponseEntity<APIResponse<UserResponse>> getUser(){

        logger.info("Get user detail request of user: {}",SecurityUtil.getCurrentUser());
        try{
            UserResponse userResponse = userService.getUser();
            logger.info("Get retrieve user detail of user: {}",SecurityUtil.getCurrentUser());
            return ResponseEntity.ok(APIResponse.success(userResponse));
        }catch (Exception e){
            logger.error("Failed to get user detail of user: {}",SecurityUtil.getCurrentUser(),e);
            throw new UsernameNotFoundException("User not found");
        }
    }
}
