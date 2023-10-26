package com.resumebuilder.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;

@Service
public class ProfileImageService {
	
//	@Value("${upload/profileImage}")
//    private String projectPath;
	//public static final String projectPath = "upload/profileImage";

	public static final String baseDirectory = "upload/profileImage/";

    public String uploadProfileImage(MultipartFile imageFile, Long userId) throws IOException, java.io.IOException {
        String originalFileName = imageFile.getOriginalFilename(); // Get the original file name
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // Extract the file extension
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".svg"};

        if (!Arrays.asList(allowedExtensions).contains(extension.toLowerCase())) {
            throw new IOException("Invalid file extension");
        }
        
        String fileName = "user_profile_" + userId + extension; // Generate a unique name for each user based on the user ID and the original extension
        String imagePath = baseDirectory + fileName;

        // Create the base directory if it doesn't exist
        File baseDir = new File(baseDirectory);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        File file = new File(imagePath);
        try (InputStream inputStream = imageFile.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }

        return fileName;
    }
    
    
    public byte[] getProfileImage(Long userId) throws IOException, FileNotFoundException, java.io.IOException {
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".svg"};
        String imagePath = null;

        for (String extension : allowedExtensions) {
            String fileName = "user_profile_" + userId + extension;
            imagePath = baseDirectory + fileName;
            File imageFile = new File(imagePath);

            if (imageFile.exists()) {
                try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
                    return IOUtils.toByteArray(fileInputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        throw new FileNotFoundException("Profile image not found");
    }

}
