package com.resumebuilder.user;

import org.springframework.http.MediaType;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	        return ResponseEntity.ok(fileName);
	    } catch (IOException e) {
	        return ResponseEntity.status(400).body("Failed to upload image: " + e.getMessage());
	    }
	}
    
	 @GetMapping(value = "/getProfileImage/{userId}", produces = MediaType.IMAGE_JPEG_VALUE)
	    public @ResponseBody byte[] getProfileImageByUserId(@PathVariable Long userId) {
	        try {
	            return profileImageService.getProfileImageByUserId(userId);
	        } catch (FileNotFoundException e) {
	            // Handle the case when the image is not found (e.g., return a default image)
	            return new byte[0];
	        } catch (Exception e) {
	            // Handle other exceptions
	            e.printStackTrace();
	            return new byte[0];
	        }
	    }

}
