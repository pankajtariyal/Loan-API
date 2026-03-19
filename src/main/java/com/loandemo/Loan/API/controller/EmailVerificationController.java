package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.service.EmailService;
import com.loandemo.Loan.API.uitls.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller responsible for handling Email Verification operations.
 *
 * <p>This controller provides APIs for:
 * <ul>
 *     <li>Verifying user email after registration</li>
 * </ul>
 *
 * <p>Users receive a verification link via email containing a token
 * and username, which is used to validate their account.
 *
 * @apiNote This endpoint is typically accessed via a link sent to the user's email.
 *
 * @implNote This controller delegates email verification logic to {@link EmailService}.
 *
 * @since 1.0
 * @author Abhishek Tadiwal
 */
@Tag(
        name = "Email APIs",
        description = "Operations related to email verification"
)
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("email")
public class EmailVerificationController {

    private final static Logger logger = LoggerFactory.getLogger(EmailVerificationController.class);

    private final EmailService emailService;

    public EmailVerificationController(EmailService emailService){
        this.emailService = emailService;
    }

    /**
     * Verifies a user's email using a token and username.
     *
     * <p>This API is triggered when the user clicks on the verification
     * link sent to their registered email address.
     *
     * @param token verification token sent via email
     * @param username username of the registered user
     * @return success or failure message as a plain string
     *
     * @apiNote This endpoint may be accessed without authentication
     * as it is part of the registration flow.
     *
     * @implSpec This method performs the following steps:
     * <ol>
     *     <li>Validates the verification token</li>
     *     <li>Matches the token with the user</li>
     *     <li>Marks the email as verified</li>
     *     <li>Returns verification status</li>
     * </ol>
     *
     * @since 1.0
     */
    @Operation(
            summary = "Verify Email",
            description = "Verify user email using token and username"
    )
    @GetMapping("verify")
    public ResponseEntity<String> verify(
            @Parameter(description = "Verification token")
            @RequestParam("token") String token,

            @Parameter(description = "Username")
            @RequestParam("user") String username){

        logger.info("Verification request for user: {}", username);
        try{
            String result = emailService.verifyEmail(token, username);
            logger.info("Verification request proceed successfully for user: {}", username);
            return ResponseEntity.ok(result);
        }catch (RuntimeException e){
            logger.error("Email verification failed for user: {}",username,e);
            throw new RuntimeException("Email Verification Failed");
        }
    }
}
