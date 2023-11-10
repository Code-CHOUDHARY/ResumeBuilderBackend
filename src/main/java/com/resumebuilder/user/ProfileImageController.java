package com.resumebuilder.user;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.jsonwebtoken.io.IOException;

@RestController
public class ProfileImageController {
	@Autowired
    private ProfileImageService profileImageService;
	
	@PostMapping("/uploadImage/{userId}")
	public ResponseEntity<String> uploadProfileImage(@PathVariable Long userId, @RequestParam("imageFile") MultipartFile imageFile) throws java.io.IOException {
	    try {
	        String fileName = profileImageService.uploadProfileImage(imageFile, userId);
	        return ResponseEntity.ok("Profie image uploaded successfully.");
	    } catch (IOException e) {
	        return ResponseEntity.status(400).body("Failed to upload image: " + e.getMessage());
	    }
	}
    
	 @GetMapping(value = "/getProfileImage/{userId}", produces = MediaType.IMAGE_JPEG_VALUE)

	 public ResponseEntity<?> getProfileImageByUserId(@PathVariable Long userId) throws IOException, java.io.IOException {
		 try {
		        byte[] profileImage = profileImageService.getProfileImageByUserId(userId);
		        return ResponseEntity.ok(profileImage);
		    } catch (FileNotFoundException e) {
		        // Return the message as plain text
		        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
		            .contentType(MediaType.TEXT_PLAIN)
		            .body("Profile image does not exist");
		    }
	    }
	 
	 @DeleteMapping("/deleteProfile/{userId}")
	    public ResponseEntity<String> deleteProfileImageByUserId(@PathVariable Long userId) throws Exception {
	        try {
	            profileImageService.softDeleteProfileImageByUserId(userId);
	            return ResponseEntity.ok("Profile image deleted successfully");
	        } catch (RuntimeException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	        }
	    }

}
