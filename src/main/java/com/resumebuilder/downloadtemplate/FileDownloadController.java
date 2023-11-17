package com.resumebuilder.downloadtemplate;



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

import io.jsonwebtoken.io.IOException;

@RestController
public class FileDownloadController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);
	
	@Autowired
	private ActivityHistoryService activityHistoryService;
	
	@GetMapping("masterdata/downloadtemplate")
    public ResponseEntity<Resource> downloadExcel() throws IOException {
		
        // Load the Excel file from the classpath
        Resource resource = new ClassPathResource("Download/Templates/Bulk_Upload_Template.xlsx");

        // Set the content type and disposition for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "Bulk_Upload_Template.xlsx");
        
//         ActivityHistory activityHistory = new ActivityHistory();
//		 activityHistory.setActivity_type("Download Template");
//		 activityHistory.setDescription("Template Downloaded for bulk upload");
//		 activityHistory.setNew_data("Employee with id "+userId+"is deleted");
//		 activityHistory.setUser(existingUser);
//		 activityHistoryService.addActivity(activityHistory, principal);
        
        // Return the file as a ResponseEntity
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(resource);
    }
}
