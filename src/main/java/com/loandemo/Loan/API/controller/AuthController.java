package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.dto.login.LoginRequestDto;
import com.loandemo.Loan.API.dto.login.LoginResponse;
import com.loandemo.Loan.API.dto.register.UserRegistrationRequest;
import com.loandemo.Loan.API.dto.register.UserRegistrationResponse;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.AuthService;
import com.loandemo.Loan.API.uitls.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller responsible for handling Authentication operations.
 *
 * <p>This controller provides APIs for:
 * <ul>
 *     <li>User login</li>
 *     <li>User registration</li>
 * </ul>
 *
 * <p>These APIs are publicly accessible and do not require authentication.
 *
 * @apiNote After successful login, a JWT token is returned which must be used
 * for accessing secured APIs.
 *
 * @implNote This controller delegates authentication logic to {@link AuthService}.
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Tag(
        name = "Auth APIs",
        description = "Authentication operations (Login & Registration)"
)
@RestController
@RequestMapping("auth")
public class AuthController {

    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * <p>This API validates user credentials (username and password)
     * and returns an authentication token if the credentials are valid.
     *
     * @param loginRequestDto request payload containing username and password
     * @return {@link APIResponse} containing {@link LoginResponse} with JWT token
     *
     * @apiNote This endpoint is publicly accessible and does not require authentication.
     *
     * @implSpec This method performs the following steps:
     * <ol>
     *     <li>Validates user credentials</li>
     *     <li>Authenticates user</li>
     *     <li>Generates JWT token</li>
     *     <li>Returns token in response</li>
     * </ol>
     * @throws IllegalAccessException
     * @since 1.0
     */
    @Operation(
            summary = "User Login",
            description = "Authenticate user and return JWT token"
    )
    @PostMapping("login")
    public ResponseEntity<APIResponse<LoginResponse>> login(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login request payload",
                    required = true
            )
            LoginRequestDto loginRequestDto) throws IllegalAccessException {
        logger.info("Received login request for user: {}", loginRequestDto.getUsername());
        try{
            ResponseEntity<APIResponse<LoginResponse>> apiResponseResponseEntity = authService.checkLogin(loginRequestDto);
            logger.info("Login successfully for user: {}", loginRequestDto.getUsername());
            return apiResponseResponseEntity;
        }catch (Exception e){
            logger.error("Failed login request for user: {}", loginRequestDto.getUsername(),e);
            throw new IllegalAccessException("User is invalid");
        }
    }

    /**
     * Registers a new user in the system.
     *
     * <p>This API allows new users to create an account by providing
     * required details such as name, email, and password.
     *
     * @param registrationRequest request payload containing user registration details
     * @return {@link APIResponse} containing {@link UserRegistrationResponse}
     *
     * @throws Exception if user registration fails (e.g., duplicate email)
     *
     * @apiNote This endpoint is publicly accessible.
     *
     * @implSpec This method performs the following steps:
     * <ol>
     *     <li>Validates registration data</li>
     *     <li>Checks for existing user</li>
     *     <li>Creates new user record</li>
     *     <li>Saves user to database</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "User Registration",
            description = "Register a new user"
    )
    @PostMapping("register")
    public ResponseEntity<APIResponse<UserRegistrationResponse>> registerUser(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration payload",
                    required = true
            )
            UserRegistrationRequest registrationRequest) throws Exception {
        logger.info("Received user registration request from: {}", registrationRequest.getUsername());
        try{
            ResponseEntity<APIResponse<UserRegistrationResponse>> user = authService.createUser(registrationRequest);
            logger.info("Proceed registration request successfully for user: {}",registrationRequest.getUsername() );
            return user;
        }catch (Exception e){
            logger.error("User registration failed for user: {}", registrationRequest.getUsername(),e);
            throw new RuntimeException("Type with different credential");
        }
    }
}

