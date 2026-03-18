package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.dto.login.LoginRequestDto;
import com.loandemo.Loan.API.dto.login.LoginResponse;
import com.loandemo.Loan.API.dto.register.UserRegistrationRequest;
import com.loandemo.Loan.API.dto.register.UserRegistrationResponse;
import com.loandemo.Loan.API.jwttoken.JwtUtils;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.repository.UserRepository;
import com.loandemo.Loan.API.responseapi.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Service class responsible for authentication and user registration operations.
 *
 * <p>This service handles core authentication functionalities such as:</p>
 * <ul>
 *     <li>User registration with validation</li>
 *     <li>Password encryption</li>
 *     <li>User login and authentication</li>
 *     <li>JWT token generation</li>
 *     <li>Email verification trigger</li>
 * </ul>
 *
 * <p>It integrates with Spring Security for authentication and uses
 * {@link JwtUtils} for token-based authentication.</p>
 *
 * <p>Security Features:</p>
 * <ul>
 *     <li>Passwords are encrypted using {@link PasswordEncoder}</li>
 *     <li>Authentication is handled via {@link AuthenticationManager}</li>
 *     <li>JWT tokens are generated for stateless authentication</li>
 * </ul>
 *
 * @author Abhishek
 */
@Service
public class AuthService {

    /**
     * Repository for user database operations.
     */
    private final UserRepository userRepository;

    /**
     * Password encoder for hashing user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Authentication manager used to authenticate user credentials.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Utility class for generating and validating JWT tokens.
     */
    private final JwtUtils jwtUtils;

    /**
     * Service responsible for sending email verification tokens.
     */
    private final EmailService emailService;

    /**
     * Constructor-based dependency injection.
     *
     * @param userRepository repository for user data
     * @param passwordEncoder password encoder
     * @param authenticationManager authentication manager
     * @param jwtUtils JWT utility class
     * @param emailService email service for verification
     */
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils,
                       EmailService emailService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager  = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
    }

    /**
     * Registers a new user in the system.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *     <li>Checks if username already exists</li>
     *     <li>Checks if email already exists</li>
     *     <li>Encodes the password</li>
     *     <li>Saves the user to the database</li>
     *     <li>Sends email verification token</li>
     * </ol>
     *
     * @param registrationRequest DTO containing user registration details
     * @return {@link ResponseEntity} containing {@link APIResponse} with {@link UserRegistrationResponse}
     * @throws Exception if any internal error occurs
     */
    @Transactional
    public ResponseEntity<APIResponse<UserRegistrationResponse>> createUser(UserRegistrationRequest registrationRequest){

            System.out.println(registrationRequest);
            boolean isUserExist = userRepository.existsUserByUsername(registrationRequest.getUsername());
            boolean isEmailExist = userRepository.existsUserByEmail(registrationRequest.getEmail());
            System.out.println("isEmailExist ::"+isEmailExist);
            if (isUserExist) {
                throw new RuntimeException("User already exits by name");
            } else if (isEmailExist) {
                throw new RuntimeException("Someone already exist with this email.");
            }

            User user = User.builder()
                    .username(registrationRequest.getUsername())
                    .email(registrationRequest.getEmail())
                    .password(passwordEncoder.encode(registrationRequest.getPassword()))
                    .role("USER")
                    .isActive(true)
                    .created_at(Timestamp.valueOf(LocalDateTime.now()))
                    .created_by(registrationRequest.getUsername())
                    .updated_at(Timestamp.valueOf(LocalDateTime.now()))
                    .updated_by(registrationRequest.getUsername())
                    .verification(false)
                    .build();

            User saveUser = userRepository.save(user);

            // Send verification email
            emailService.saveAndSendToken(saveUser);

            UserRegistrationResponse registrationResponse = UserRegistrationResponse.builder()
                    .username(saveUser.getUsername())
                    .build();

            return ResponseEntity.ok(
                    APIResponse.success(
                            registrationResponse,
                            "User register successfully and email verification link sent to you email."
                    )
            );
    }

    /**
     * Authenticates user credentials and generates JWT token.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *     <li>Authenticates user using {@link AuthenticationManager}</li>
     *     <li>Extracts user role from granted authorities</li>
     *     <li>Generates JWT token</li>
     *     <li>Returns login response with token</li>
     * </ol>
     *
     * @param loginRequestDto DTO containing username and password
     * @return {@link ResponseEntity} containing {@link APIResponse} with {@link LoginResponse}
     */
    public ResponseEntity<APIResponse<LoginResponse>> checkLogin(LoginRequestDto loginRequestDto) throws IllegalAccessException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {

            String role = authentication.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse(null);

            LoginResponse loginResponse = LoginResponse.builder()
                    .username(authentication.getName())
                    .token(jwtUtils.generateToken(authentication.getName(), role))
                    .build();

            return ResponseEntity.ok(
                    APIResponse.success(loginResponse, "user is authenticate")
            );

        } else {
            throw new IllegalAccessException("User in not authenticate");
        }
    }
}
