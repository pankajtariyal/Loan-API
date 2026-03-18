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
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Optional;

/**
 * Service class responsible for handling email-related operations
 * such as sending verification emails and validating email tokens.
 *
 * <p>This service plays a crucial role in user account verification.</p>
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Generate email verification tokens</li>
 *     <li>Send verification emails to users</li>
 *     <li>Validate verification tokens</li>
 *     <li>Activate user accounts after successful verification</li>
 * </ul>
 *
 * <p>Verification Flow:</p>
 * <pre>
 * User Registration →
 * Generate Token →
 * Send Email with Link →
 * User Clicks Link →
 * Verify Token →
 * Activate User Account
 * </pre>
 *
 * @author Abhishek
 */
@Service
public class EmailService {

    /**
     * Repository for managing email verification tokens.
     */
    private final EmailRepository emailRepository;

    /**
     * Spring's mail sender used to send emails.
     */
    private final JavaMailSender mailSender;

    /**
     * Repository for managing user data.
     */
    private final UserRepository userRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param mailSender JavaMailSender instance
     * @param emailRepository Email repository
     * @param userRepository User repository
     */
    public EmailService(JavaMailSender mailSender,
                        EmailRepository emailRepository,
                        UserRepository userRepository){
        this.mailSender = mailSender;
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
    }

    /**
     * Generates a verification token, saves it in the database,
     * and sends a verification email to the user.
     *
     * <p>This method performs the following steps:</p>
     * <ul>
     *     <li>Generates a unique email verification token</li>
     *     <li>Saves the token associated with the user</li>
     *     <li>Constructs a verification link</li>
     *     <li>Sends the verification email</li>
     * </ul>
     *
     * <p>The verification link contains:</p>
     * <ul>
     *     <li>Token</li>
     *     <li>Username</li>
     * </ul>
     *
     * @param user the user for whom the verification email is to be sent
     */
    @Transactional
    public void saveAndSendToken(User user){
        String token = saveVerificationToken(user);
        sendVerificationEmail(user,token);
    }

    @Async
    public void sendVerificationEmail(User user, String token) {
        try{
            String link  = "http://localhost:122/api/v1/email/verify?token=" + token+"&user="+user.getUsername();
            String frame = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "  <body style=\"font-family:Arial,sans-serif; background-color:#f5f5f5; margin:0; padding:20px;\">\n" +
                    "    <div style=\"background-color:#ffffff; border:1px solid #ccc; border-radius:10px; padding:20px; max-width:500px; margin:0 auto;\">\n" +
                    "      <h3 style=\"margin:0 0 10px 0; color:#333;\">Email Verification</h3>\n" +
                    "      <p style=\"font-size:14px; color:#555; line-height:1.5; margin:0 0 10px 0;\">\n" +
                    "        This code is valid for 15 minutes and can only be used once.\n" +
                    "      </p>\n" +
                    "      <p style=\"font-size:14px; color:#555; line-height:1.5; margin:0 0 10px 0;\">\n" +
                    "        Please don't share this link with anyone: we'll never ask for it on the phone or via email.\n" +
                    "      </p>\n" +
                    "\n" +
                    "      <a href=\"http://localhost:122/api/v1/email/verify?token="+token+"&user="+user.getUsername()+"\"\n" +
                    "         style=\"display:inline-block; padding:10px 20px; margin-top:10px; background-color:#4CAF50; color:#fff; text-decoration:none; border-radius:5px;\">\n" +
                    "         Verify Now\n" +
                    "      </a>\n" +
                    "\n" +
                    "      <div style=\"margin-top:15px;\">\n" +
                    "        <p style=\"font-size:14px; color:#555; margin:0;\">Thanks,</p>\n" +
                    "        <p style=\"font-size:14px; color:#555; margin:0;\">The Loan Team</p>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>\n";
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setTo(user.getEmail());
            helper.setSubject("Email Verification:");
            helper.setText(frame,true);
            mailSender.send(message);
        }catch (Exception e){
            throw new RuntimeException("Exception occur");
        }
    }

    @Transactional
    public String saveVerificationToken(User user){
        try{
            String token = EmailUtil.generateEmailVerificationToken();
            Email email = Email.builder()
                    .token(token)
                    .user(user)
                    .used(false)
                    .build();
            emailRepository.save(email);
            return token;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Verifies the user's email using the provided token and username.
     *
     * <p>This method performs the following validations:</p>
     * <ul>
     *     <li>Checks if the token exists</li>
     *     <li>Ensures the token is not already used</li>
     *     <li>Checks if the user exists</li>
     *     <li>Ensures the user is not already verified</li>
     * </ul>
     *
     * <p>On successful verification:</p>
     * <ul>
     *     <li>Marks the token as used</li>
     *     <li>Updates user's verification status</li>
     * </ul>
     *
     * @param token verification token sent via email
     * @param username username associated with the token
     * @return success message if verification is completed
     * @throws RuntimeException if token is invalid, already used,
     *                          user not found, or already verified
     */
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
