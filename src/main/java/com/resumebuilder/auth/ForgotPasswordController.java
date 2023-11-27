package com.resumebuilder.auth;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.DTO.ForgetPasswordResponse;
import com.resumebuilder.DTO.ForgotPassword;
import com.resumebuilder.exception.TokenExpiredException;

@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {
	 @Autowired
	 private ForgotPasswordService forgotPasswordService;
	 
	 @PostMapping("/forgotPassword/request")
	    public ResponseEntity<String> requestReset(@RequestParam String email) {
	        forgotPasswordService.processForgotPassword(email);
	        return ResponseEntity.ok("Forgot password link sent successfully.");
	    }

//	    @PostMapping("/resetPassword")
//	    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody ForgotPassword forgotPassword) {
//	        forgotPasswordService.resetPassword(token, forgotPassword);
//	        return ResponseEntity.ok("Password reset successfully.");
//	    }
	 
	 
	 @PostMapping("/resetPassword")
	 public ResponseEntity<ForgetPasswordResponse> resetPassword(@RequestParam String token, @RequestBody ForgotPassword forgotPassword) {
	     try {
	         // Call the resetPassword method in ForgotPasswordService
	         forgotPasswordService.resetPassword(token, forgotPassword);

	         ForgetPasswordResponse response = ForgetPasswordResponse.builder()
	                 .statusType(HttpStatus.ACCEPTED)
	                 .messageType("Success")
	                 .message("Password reset successfully.")
	                 .status(true)
	                 .build();

	         return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	     } catch (Exception e) {
	         // Handle other exceptions if needed
	         ForgetPasswordResponse response = ForgetPasswordResponse.builder()
	                 .statusType(HttpStatus.INTERNAL_SERVER_ERROR)
	                 .messageType("Error")
	                 .message("Internal Server Error")
	                 .status(false)
	                 .build();

	         return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	     }
	 }

	 


	    @PostMapping("/checkTokenExpiry")
	    public ResponseEntity<ForgetPasswordResponse> checkToken(@RequestParam String token) {
	        try {
	            // Call the checkTokenExpire method in ForgotPasswordService
	            forgotPasswordService.checkTokenExpire(token);

	            // If the token is still valid, redirect to the Reset Password API
	            // You can customize the URL according to your application
	            String resetPasswordUrl = "/resetPassword?token=" + token;

	            ForgetPasswordResponse response = ForgetPasswordResponse.builder()
	                    .statusType(HttpStatus.ACCEPTED)
	                    .messageType("Success")
	                    .message("Token is still valid")
	                    .status(true)
	                    .data(resetPasswordUrl)
	                    .build();

	            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	        } catch (TokenExpiredException e) {
	            ForgetPasswordResponse response = ForgetPasswordResponse.builder()
	                    .statusType(HttpStatus.BAD_REQUEST)
	                    .messageType("Error")
	                    .message("Your password reset link appears to be invalid. Please request a new link below.")
	                    .status(false)
	                    .build();

	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	        } catch (Exception e) {
	            // Handle other exceptions if needed
	            ForgetPasswordResponse response = ForgetPasswordResponse.builder()
	                    .statusType(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .messageType("Error")
	                    .message("Internal Server Error")
	                    .status(false)
	                    .build();

	            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    
}
