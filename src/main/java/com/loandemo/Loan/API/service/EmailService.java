package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.modul.Email;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.repository.EmailRepository;
import com.loandemo.Loan.API.repository.UserRepository;
import com.loandemo.Loan.API.responseapi.APIResponse;
import com.loandemo.Loan.API.uitls.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    public EmailService(JavaMailSender mailSender, EmailRepository emailRepository, UserRepository userRepository){
        this.mailSender = mailSender;
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public void saveAndSendToken(User user){
        try{
            String token = EmailUtil.generateEmailVerificationToken();
            Email email = Email.builder()
                    .token(token)
                    .user(user)
                    .used(false)
                    .build();
            String link  = "http://localhost:122/v1/email/verify?token=" + email.getToken()+"&user="+user.getUsername();
            Email savedToken = emailRepository.save(email);
            SimpleMailMessage message =  new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Email Verification.");
            message.setText("Click link to verify: "  + link);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public String verifyEmail(String token, String username) {
            Email email = emailRepository.findByToken(token)
                    .orElseThrow(()-> new RuntimeException("InvalidToken Verification Token"));
            if(email.isUsed()){
                throw new RuntimeException("Token already used.");
            }

            User user = userRepository.findByUsername(username)
                    .orElseThrow(()->new RuntimeException("User not found."));
            if(user.isVerification()){
                throw new RuntimeException("User already verified");
            }

            email.setUsed(true);
            user.setVerification(true);
            emailRepository.save(email);
            userRepository.save(user);

            return "Email verified successfully";
    }


}
