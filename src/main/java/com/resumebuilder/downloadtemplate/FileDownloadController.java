package com.resumebuilder.downloadtemplate;



import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.activityhistory.ActivityHistory;
import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserService;

import io.jsonwebtoken.io.IOException;

@RestController
public class FileDownloadController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);
	
	@Autowired
	private ActivityHistoryService activityHistoryService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("masterdata/downloadtemplate")
    public ResponseEntity<Resource> downloadExcel(Principal principal) throws IOException {
		
		User user = userService.getUserByEmail(principal.getName());
		
        // Load the Excel file from the classpath
        Resource resource = new ClassPathResource("Download/Templates/Bulk_Upload_Template.xlsx");

        // Set the content type and disposition for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "Bulk_Upload_Template.xlsx");
        
         ActivityHistory activityHistory = new ActivityHistory();
		 activityHistory.setActivity_type("Download Template");
		 activityHistory.setDescription("Template downloaded for bulk upload");
		 activityHistory.setActivity_by(user.getUser_id());
		 activityHistoryService.addActivity(activityHistory, principal);
        
        // Return the file as a ResponseEntity
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(resource);
    }
}
