package com.loandemo.Loan.API.controller;

import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("email")
public class EmailVerificationController {

    private final EmailService emailService;

    public EmailVerificationController(EmailService emailService){
        this.emailService = emailService;
    }

    @GetMapping("verify")
    public ResponseEntity<String> verify(@RequestParam("token") String token, @RequestParam("user") String username){
        String result =  emailService.verifyEmail(token,username);
        return ResponseEntity.ok(result);
    }
}
