package com.resumebuilder.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;

@Service
public class ProfileImageService {
	
	@Autowired
	private UserRepository userRepository;
	
	public static String fileSeparator = System.getProperty("file.separator");
	public static final String baseDirectory = "upload"+fileSeparator;
	
	public String uploadProfileImage(MultipartFile imageFile, Long userId) throws IOException, java.io.IOException {
        String originalFileName = imageFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".svg"};

        if (!Arrays.asList(allowedExtensions).contains(extension.toLowerCase())) {
            throw new IOException("Invalid file extension");
        }

        // Create the directory structure based on user's employee_id
        String userDirectory = baseDirectory + userId + fileSeparator;
        String profileImageDirectory = userDirectory + "profileImage"+fileSeparator;

        File userDir = new File(userDirectory);
        File profileDir = new File(profileImageDirectory);

        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        if (!profileDir.exists()) {
            profileDir.mkdirs();
        }

        String uniqueFileName = UUID.randomUUID().toString() + extension;
        String imagePath = profileImageDirectory + uniqueFileName;

        File file = new File(imagePath);
        try (InputStream inputStream = imageFile.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
     // Update the user's user_image field with the image path
        String imagePathInDatabase = profileImageDirectory + uniqueFileName;
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setUser_image(imagePathInDatabase);
            userRepository.save(user);
        }

        return uniqueFileName;
    }

	
//	public byte[] getProfileImageByUserId(Long userId) throws IOException, FileNotFoundException, java.io.IOException {
//        User user = userRepository.findById(userId).orElse(null);
//        if (user != null) {
//            String imagePath = user.getUser_image();
//
//            if (imagePath != null && !imagePath.isEmpty()) {
//                File imageFile = new File(imagePath);
//
//                try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
//                    return IOUtils.toByteArray(fileInputStream);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    throw e;
//                }
//            }
//        }
//
//        throw new FileNotFoundException("Profile image not found");
//    }

	public byte[] getProfileImageByUserId(Long userId) throws IOException, FileNotFoundException, java.io.IOException {
	    User user = userRepository.findById(userId).orElse(null);
	    
	    if (user != null) {
	        String imagePath = user.getUser_image();
	        
	        if (imagePath != null && !imagePath.isEmpty()) {
	            File imageFile = new File(imagePath);
	            
	            try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
	                return IOUtils.toByteArray(fileInputStream);
	            } catch (IOException e) {
	                e.printStackTrace();
	                throw e;
	            }
	        } else {
	            throw new FileNotFoundException("Profile image does not exist");
	        }
	    } else {
	        throw new FileNotFoundException("User not found");
	    }
	}
	
	public void softDeleteProfileImageByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            String imagePath = user.getUser_image();

            if (imagePath != null && !imagePath.isEmpty()) {
                // Rename the image file to mark it as deleted (e.g., by adding "_deleted" to the filename)
                File imageFile = new File(imagePath);
                String deletedImagePath = imagePath.replaceFirst("\\.", "_deleted.");
                File deletedImageFile = new File(deletedImagePath);

                if (imageFile.exists() && imageFile.isFile()) {
                    if (imageFile.renameTo(deletedImageFile)) {
                        // Image file marked as deleted successfully

                        // Update the user's user_image field to point to the deleted image
                        user.setUser_image(null);
                        userRepository.save(user);
                    } else {
                        // Failed to mark the image as deleted
                        throw new RuntimeException("Failed to mark the profile image as deleted");
                    }
                } else {
                    // Image file does not exist
                    throw new RuntimeException("Profile image not found");
                }
            } else {
                // User does not have a profile image
                throw new RuntimeException("User does not have a profile image");
            }
        } else {
            // User not found
            throw new RuntimeException("User not found");
        }
    }

}
