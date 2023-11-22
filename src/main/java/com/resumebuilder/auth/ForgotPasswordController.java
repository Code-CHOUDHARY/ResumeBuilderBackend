package com.resumebuilder.auth;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.DTO.ForgotPassword;

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

	    @PostMapping("/resetPassword")
	    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody ForgotPassword forgotPassword) {
	        forgotPasswordService.resetPassword(token, forgotPassword);
	        return ResponseEntity.ok("Password reset successfully.");
	    }

}
