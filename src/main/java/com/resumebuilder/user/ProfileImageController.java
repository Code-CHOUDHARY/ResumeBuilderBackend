package com.resumebuilder.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import io.jsonwebtoken.io.IOException;

@RestController
public class ProfileImageController {
	@Autowired
    private ProfileImageService profileImageService;

    @PostMapping("/uploadImage/{userId}")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("image") MultipartFile imageFile, @PathVariable Long userId) throws IllegalStateException, java.io.IOException {
        try {
            profileImageService.uploadProfileImage(imageFile, userId);
            return ResponseEntity.ok("Profile image uploaded successfully." );
        } catch (IOException e) {
            // Handle exceptions
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Error uploading profile image.");
        }
    }
    
    @GetMapping("/profileImage/{userId}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long userId) throws IOException, java.io.IOException {
        try {
            byte[] imageBytes = profileImageService.getProfileImage(userId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) 
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
