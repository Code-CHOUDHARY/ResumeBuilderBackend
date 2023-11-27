package com.resumebuilder.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.resumebuilder.DTO.ForgotPassword;
import com.resumebuilder.exception.ForgotPasswordException;
import com.resumebuilder.exception.TokenExpiredException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class ForgotPasswordService {
	
	private final Logger logger = LogManager.getLogger(ForgotPasswordService.class);

	@Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
//    @Value("${app.login.url}")  
//    private String loginUrl;
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void processForgotPassword(String email) {
        // Check if the user with the given email exists
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ForgotPasswordException("User not found with email: " + email));

        // Check if there is an existing unused token for the user
        Optional<PasswordResetToken> existingToken = resetTokenRepository.findByUserAndUsedIsFalse(user);

        if (existingToken.isPresent()) {
            // Update the existing token's details
            existingToken.get().setExpiryDate(LocalDateTime.now().plusHours(1));
            resetTokenRepository.save(existingToken.get());
            sendResetLink(email, existingToken.get().getToken());
        } else {
            // Generate a unique token
            String token = UUID.randomUUID().toString();

            LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);
            PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryDate);
            
            resetTokenRepository.save(resetToken);

            sendResetLink(email, token);
        }
    }


    public void resetPassword(String token, ForgotPassword forgotPassword) {
        // Find the token in the database
        PasswordResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenExpiredException("Your password reset link appears to be invalid. Please request a new link."));

        // Check if the token is still valid
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        // Set the new password for the user
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(forgotPassword.getNewPassword()));
        resetToken.setUsed(true);
        // Remove the reset token from the database
        resetTokenRepository.delete(resetToken);
        // Update the user in the database
        userRepository.save(user);
        logger.info("Reset password successfully");
    }
    
    
    public void sendResetLink(String email, String token) {
        try {
            // Find the user by email
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

            // Construct the email content
            String senderName = "QW Resume Builder";
            String subject = "Password Reset Link";
//            String resetLink = loginUrl + "/reset-password?token=" + token;

//            String resetLink = "reset-password?token=" + token;
//            String checkTokenLink = "checkTokenExpiry?token=" + token;
//            String checkTokenLink = "http://localhost:3011/ResumBuilder/auth/verify";
            String verifyLink = "http://localhost:3011/ResumBuilder/auth/verify?token=" + token;

            String content = "Dear " + user.getFull_name() + ",<br>"
                    + "You have requested to reset your password for QW Resume Builder. "
                    + "Please click the link below to reset your password:<br>"
                    + "<a href=\"" + verifyLink + "\">Reset Password</a><br><br>"
                    + "If you did not request this, please ignore this email.<br>";

            content += "<p>Thank you,<br>" + "QW Resume Builder.</p>";

            // Create and send the email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom("pratikshawakekar94@gmail.com", senderName);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error sending reset link email.");
        }
    }
    
//    
//    public void checkTokenExpire(String token) {
//    	
//    	 PasswordResetToken resetToken = resetTokenRepository.findByToken(token)
//                 .orElseThrow(() -> new TokenExpiredException("Your password reset link appears to be invalid. Please request a new link."));
//
//         // Check if the token is still valid
//         if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
//             throw new RuntimeException("Token expired");
//         }else {
//        	 
//         }
    
    public void checkTokenExpire(String token) {
        try {
            // Log before fetching the token
        	logger.info("Fetching token from the repository for token: {}", token);

            PasswordResetToken resetToken = resetTokenRepository.findByToken(token)
                    .orElseThrow(() -> new TokenExpiredException("Your password reset link appears to be invalid. Please request a new link."));

            // Log after fetching the token
            logger.info("Token fetched successfully");

            // Check if the token is still valid
            if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new TokenExpiredException("Token expired");
            } else {
                // Additional logic if needed when the token is still valid
            }
        } catch (Exception e) {
            // Log the exception
        	logger.error("Exception occurred in checkTokenExpire: {}", e.getMessage(), e);
            throw e; // Rethrow the exception to propagate it up the stack
        }
    }


    	
    }
    
    

