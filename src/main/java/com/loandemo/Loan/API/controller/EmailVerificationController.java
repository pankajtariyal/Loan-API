package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name="Email APIs",
        description = "Operation related to Email")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("email")
public class EmailVerificationController {

    private final EmailService emailService;

    public EmailVerificationController(EmailService emailService){
        this.emailService = emailService;
    }

    @Operation(summary = "Verify Email", description = "Verify Email when user register")
    @GetMapping("verify")
    public ResponseEntity<String> verify(
            @Parameter(description = "Authentication token for verification") @RequestParam("token") String token,
            @Parameter(description = "Username") @RequestParam("user") String username){
        String result =  emailService.verifyEmail(token,username);
        return ResponseEntity.ok(result);
    }
}
