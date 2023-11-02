package com.resumebuilder.downloadhistory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.exception.ResumeNotFoundException;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("downloadhistory")
public class DownloadHistoryController {
	
	private static final String UPLOAD_DIR = "Upload/E620/Resume/"; // Directory to store files
	
	@Autowired
	private DownloadHistoryService downloadHistoryService;
	
	/**
     * Adds a new download history record.
     *
     * @param downloadHistory The download history to be added.
     * @return The added download history record.
     */
    @PostMapping("/add")
    public DownloadHistory addDownloadHistory(@RequestBody DownloadHistory downloadHistory,Principal principal ) {
        return downloadHistoryService.saveDownloadHistory(downloadHistory,principal);
    }
	
    /**
     * Retrieves a list of all download history records.
     *
     * @return A list of download history records.
     */
    @GetMapping("/all")
    public List<DownloadHistory> getAllDownloadHistories() {
        return downloadHistoryService.getAllDownloadHistories();
    }
	
	/**
     * Endpoint for downloading a file.
     * @param fileName The name of the file to download.
     * @param response The HttpServletResponse to stream the file to the client.
     */
	@GetMapping("/download/{fileName}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
		// Resource resource = new UrlResource("file:" + UPLOAD_DIR + "/" + fileName);
		Resource resource = new ClassPathResource(UPLOAD_DIR + fileName);
		
		if (resource.exists() && resource.isReadable()) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

			return ResponseEntity.ok().headers(headers).body(resource);
		} else {
			throw new ResumeNotFoundException("Resume not found with file name "+ fileName);
		}
	}
    
    /**
     * Endpoint for deleting a file.
     * @param fileName The name of the file to delete.
     * @return A message indicating the result of the delete operation.
     */

	@DeleteMapping("/delete/{fileName}")
	@PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        try {
            Resource resource = new ClassPathResource(UPLOAD_DIR + fileName);

            if (resource.exists()) {
                if (resource.getFile().delete()) {
                    return ResponseEntity.ok("File deleted successfully!");
                } else {
                    return ResponseEntity.badRequest().body("File deletion failed.");
                }
            } else throw new ResumeNotFoundException("Resume not found with file name "+ fileName);
        } catch (IOException e) {
            // Handle exceptions
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
	
}
